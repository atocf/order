package br.com.atocf.order.processor.service;

import br.com.atocf.order.processor.config.RabbitMQConfig;
import br.com.atocf.order.processor.dto.OrderRequest;
import br.com.atocf.order.processor.model.Order;
import br.com.atocf.order.processor.util.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.prometheus.client.Counter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
public class OrderConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

    private final ObjectMapper objectMapper;
    private final RabbitMQConfig rabbitMQConfig;
    private final RedissonClient redissonClient;
    private final MongoTemplate mongoTemplate;

    private final Counter duplicateOrderCounter = Counter.build()
            .name("duplicate_orders_total")
            .help("Total de pedidos duplicados detectados.")
            .register();

    private final Counter errorOrderCounter = Counter.build()
            .name("error_orders_total")
            .help("Total de pedidos com erros detectados.")
            .register();

    public OrderConsumer(ObjectMapper objectMapper, RabbitMQConfig rabbitMQConfig,
                         RedissonClient redissonClient, MongoTemplate mongoTemplate) {
        this.objectMapper = objectMapper;
        this.rabbitMQConfig = rabbitMQConfig;
        this.redissonClient = redissonClient;
        this.mongoTemplate = mongoTemplate;
    }

    @RabbitListener(queues = "#{rabbitMQConfig.queueName}")
    public void consumeOrderMessage(String message) {
        try {
            logger.info("Mensagem recebida da fila '{}': {}", rabbitMQConfig.getQueueName(), message);

            OrderRequest orderRequest = objectMapper.readValue(message, OrderRequest.class);
            logger.info("Pedido deserializado com sucesso: {}", orderRequest);

            processOrder(orderRequest);

        } catch (JsonProcessingException e) {
            logger.error("Erro ao deserializar mensagem: {}", message, e);
            errorOrderCounter.inc();
        } catch (Exception e) {
            logger.error("Erro inesperado ao processar mensagem: {}", message, e);
            errorOrderCounter.inc();
        }
    }

    private void processOrder(OrderRequest orderRequest) {
        String lockKey = "order-lock-" + orderRequest.getOrderId();
        RLock lock = redissonClient.getLock(lockKey);

        boolean isLockAcquired = false;

        try {
            isLockAcquired = lock.tryLock(5, 10, TimeUnit.SECONDS);

            if (isLockAcquired) {
                boolean isOrderProcessed = upsertOrder(orderRequest);

                if (isOrderProcessed) {
                    logger.info("Pedido processado com sucesso: {}", orderRequest.getOrderId());
                } else {
                    logger.warn("Pedido duplicado detectado: {}", orderRequest.getOrderId());
                    duplicateOrderCounter.inc();
                }
            } else {
                logger.warn("Pedido já está sendo processado por outro pod: {}", orderRequest.getOrderId());
                duplicateOrderCounter.inc();
            }
        } catch (InterruptedException e) {
            logger.error("Erro ao tentar adquirir o bloqueio para o pedido: {}", orderRequest.getOrderId(), e);
            errorOrderCounter.inc();
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("Erro inesperado ao processar pedido: {}", orderRequest.getOrderId(), e);
            errorOrderCounter.inc();
        } finally {
            if (isLockAcquired) {
                lock.unlock();
                logger.info("Bloqueio liberado para o pedido: {}", orderRequest.getOrderId());
            }
        }
    }

    private boolean upsertOrder(OrderRequest orderRequest) {
        try {
            Order order = mapToOrder(orderRequest);

            Order existingOrder = mongoTemplate.findAndModify(
                    query(where("orderId").is(orderRequest.getOrderId())),
                    new Update()
                            .setOnInsert("orderId", order.getOrderId())
                            .setOnInsert("customer", order.getCustomer())
                            .setOnInsert("products", order.getProducts())
                            .setOnInsert("createdAt", order.getCreatedAt())
                            .setOnInsert("entryAt", order.getEntryAt())
                            .setOnInsert("updatedAt", order.getUpdatedAt())
                            .setOnInsert("status", order.getStatus()),
                    FindAndModifyOptions.options().upsert(true).returnNew(false),
                    Order.class
            );

            return existingOrder == null;
        } catch (Exception e) {
            logger.warn("Erro ao tentar inserir o pedido no banco: {}", orderRequest.getOrderId(), e);
            duplicateOrderCounter.inc();
            return false;
        }
    }

    private Order mapToOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderId(orderRequest.getOrderId());
        order.setCustomer(orderRequest.getCustomer());
        order.setProducts(orderRequest.getProducts());
        order.setCreatedAt(DateUtils.convertStringToDate(orderRequest.getCreatedAt()));
        order.setEntryAt(new Date());
        order.setUpdatedAt(new Date());
        order.calculateTotalValue();
        return order;
    }
}