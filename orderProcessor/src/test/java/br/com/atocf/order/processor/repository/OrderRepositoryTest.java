package br.com.atocf.order.processor.repository;

import br.com.atocf.order.processor.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        orderRepository.deleteAll();
    }

    @Test
    public void testExistsByOrderId_ShouldReturnFalse_WhenOrderIdDoesNotExist() {
        String orderId = "nonExistingOrderId";
        boolean exists = orderRepository.existsByOrderId(orderId);
        assertThat(exists).isFalse();
    }

    @Test
    public void testExistsByOrderId_ShouldReturnTrue_WhenOrderIdExists() {
        String orderId = "existingOrderId";
        Order order = new Order();
        order.setOrderId(orderId);
        orderRepository.save(order);

        boolean exists = orderRepository.existsByOrderId(orderId);

        assertThat(exists).isTrue();
    }
}