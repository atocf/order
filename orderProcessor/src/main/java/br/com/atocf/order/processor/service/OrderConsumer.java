package br.com.atocf.order.processor.service;

import br.com.atocf.order.processor.config.RabbitMQConfig;
import br.com.atocf.order.processor.dto.OrderRequest;
import br.com.atocf.order.processor.exception.DuplicateOrderException;
import br.com.atocf.order.processor.model.Order;
import br.com.atocf.order.processor.model.OrderStatus;
import br.com.atocf.order.processor.repository.OrderRepository;
import br.com.atocf.order.processor.util.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OrderConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;
    private final RabbitMQConfig rabbitMQConfig;

    public OrderConsumer(ObjectMapper objectMapper, OrderRepository orderRepository, RabbitMQConfig rabbitMQConfig) {
        this.objectMapper = objectMapper;
        this.orderRepository = orderRepository;
        this.rabbitMQConfig = rabbitMQConfig;
    }

    @RabbitListener(queues = "#{rabbitMQConfig.queueName}")
    public void consumeOrderMessage(String message) throws JsonProcessingException {
        try {
            logger.info("Mensagem recebida da fila '{}': {}", rabbitMQConfig.getQueueName(), message);

            // Deserializar a mensagem recebida
            OrderRequest orderRequest = objectMapper.readValue(message, OrderRequest.class);
            logger.info("Pedido deserializado com sucesso: {}", orderRequest);

            // Processar o pedido
            processOrder(orderRequest);

        } catch (Exception e) {
            logger.error("Erro ao processar mensagem da fila '{}': {}", rabbitMQConfig.getQueueName(), e.getMessage(), e);
            throw e;
        }
    }

    private void processOrder(OrderRequest orderRequest) {
        if (orderRepository.existsByOrderId(orderRequest.getOrderId())) {
            throw new DuplicateOrderException("Pedido duplicado detectado: " + orderRequest.getOrderId());
        }

        Order order = mapToOrder(orderRequest);
        orderRepository.save(order);
        logger.info("Pedido recebido e salvo com sucesso: {}", order.getOrderId());

        order.calculateTotalValue();
        orderRepository.save(order);
        logger.info("Pedido calculado e salvo com sucesso: {}", order.getOrderId());
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