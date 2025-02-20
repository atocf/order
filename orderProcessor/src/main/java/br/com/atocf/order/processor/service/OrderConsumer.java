package br.com.atocf.order.processor.service;

import br.com.atocf.order.processor.config.RabbitMQConfig;
import br.com.atocf.order.processor.dto.OrderRequest;
import br.com.atocf.order.processor.model.Order;
import br.com.atocf.order.processor.model.OrderStatus;
import br.com.atocf.order.processor.repository.OrderRepository;
import br.com.atocf.order.processor.util.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.prometheus.client.Counter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class OrderConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;
    private final RabbitMQConfig rabbitMQConfig;
    private final RedissonClient redissonClient;

    private final Counter duplicateOrderCounter = Counter.build()
            .name("duplicate_orders_total")
            .help("Total de pedidos duplicados detectados.")
            .register();

    private final Counter errorOrderCounter = Counter.build()
            .name("error_orders_total")
            .help("Total de pedidos com erros detectados.")
            .register();

    public OrderConsumer(ObjectMapper objectMapper, OrderRepository orderRepository, RabbitMQConfig rabbitMQConfig, RedissonClient redissonClient) {
        this.objectMapper = objectMapper;
        this.orderRepository = orderRepository;
        this.rabbitMQConfig = rabbitMQConfig;
        this.redissonClient = redissonClient;
    }

    @RabbitListener(queues = "#{rabbitMQConfig.queueName}")
    public void consumeOrderMessage(String message) throws JsonProcessingException {
        try {
            logger.info("Mensagem recebida da fila '{}': {}", rabbitMQConfig.getQueueName(), message);

            OrderRequest orderRequest = objectMapper.readValue(message, OrderRequest.class);
            logger.info("Pedido deserializado com sucesso: {}", orderRequest);

            processOrder(orderRequest);

        } catch (Exception e) {
            logger.error("Erro ao processar mensagem da fila '{}': {}", rabbitMQConfig.getQueueName(), e.getMessage(), e);
            throw e;
        }
    }

    private void processOrder(OrderRequest orderRequest) {
        String lockKey = "order-lock-" + orderRequest.getOrderId();
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
                try {
                    if (orderRepository.existsByOrderId(orderRequest.getOrderId())) {
                        logger.error("Pedido duplicado detectado: {}", orderRequest.getOrderId());
                        duplicateOrderCounter.inc();
                        return;
                    }

                    Order order = mapToOrder(orderRequest);
                    orderRepository.save(order);
                    logger.info("Pedido recebido e salvo com sucesso: {}", order.getOrderId());

                    order.calculateTotalValue();
                    orderRepository.save(order);
                    logger.info("Pedido calculado e salvo com sucesso: {}", order.getOrderId());
                } finally {
                    lock.unlock();
                }
            } else {
                logger.warn("Pedido já está sendo processado por outro pod: {}", orderRequest.getOrderId());
                duplicateOrderCounter.inc();
            }
        } catch (Exception e) {
            logger.error("Erro ao processar pedido: {}", orderRequest.getOrderId(), e);
            errorOrderCounter.inc();
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
        order.setStatus(OrderStatus.RECEIVED);
        return order;
    }
}