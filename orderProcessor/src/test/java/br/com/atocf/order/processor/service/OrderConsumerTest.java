package br.com.atocf.order.processor.service;

import br.com.atocf.order.processor.config.RabbitMQConfig;
import br.com.atocf.order.processor.dto.OrderRequest;
import br.com.atocf.order.processor.model.Customer;
import br.com.atocf.order.processor.model.Order;
import br.com.atocf.order.processor.model.OrderStatus;
import br.com.atocf.order.processor.model.Product;
import br.com.atocf.order.processor.repository.OrderRepository;
import br.com.atocf.order.processor.util.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderConsumerTest {

    @InjectMocks
    private OrderConsumer orderConsumer;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RabbitMQConfig rabbitMQConfig;

    @Mock
    private Logger logger;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConsumeOrderMessageSuccessfully() throws Exception {
        String message = """
            {
                "orderId": "ORD123456",
                "customer": {
                    "id": "CUST001",
                    "name": "Maria Silva"
                },
                "products": [
                    {
                        "productId": "PROD001",
                        "name": "Teclado Gamer",
                        "quantity": 1,
                        "unitPrice": 250.0
                    },
                    {
                        "productId": "PROD002",
                        "name": "Mouse Gamer",
                        "quantity": 2,
                        "unitPrice": 120.0
                    }
                ],
                "createdAt": "2025-02-18T01:26:07Z"
            }
        """;

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderId("ORD123456");
        orderRequest.setCreatedAt("2025-02-18T01:26:07Z");
        orderRequest.setCustomer(new Customer());
        orderRequest.setProducts(Arrays.asList(
                new Product("PROD001", "Teclado Gamer", 1, 250.0),
                new Product("PROD002", "Mouse Gamer", 2, 120.0)
        ));
        when(objectMapper.readValue(message, OrderRequest.class)).thenReturn(orderRequest);

        when(orderRepository.existsByOrderId("ORD123456")).thenReturn(false);

        orderConsumer.consumeOrderMessage(message);

        verify(orderRepository, times(1)).existsByOrderId("ORD123456");
        verify(orderRepository, times(2)).save(orderCaptor.capture()); // O pedido será salvo duas vezes

        Order savedOrder = orderCaptor.getAllValues().get(0);
        assertEquals("ORD123456", savedOrder.getOrderId(), "O ID do pedido deve ser 'ORD123456'");
        assertEquals(DateUtils.convertStringToDate("2025-02-18T01:26:07Z"), savedOrder.getCreatedAt());
        assertNotNull(savedOrder.getEntryAt(), "A data de entrada deve ser preenchida");
        assertEquals(490.0, savedOrder.getTotalValue(), "O valor total deve ser 490.0 (250.0 + 2 * 120.0)");
        assertEquals(OrderStatus.CALCULATED, savedOrder.getStatus(), "O status final deve ser 'CALCULATED'");
    }

    @Test
    void testConsumeOrderMessageDuplicateOrder() throws Exception {
        String message = """
        {
            "orderId": "ORD123456",
            "customer": {
                "id": "CUST001",
                "name": "Maria Silva"
            },
            "products": [
                {
                    "productId": "PROD001",
                    "name": "Teclado Gamer",
                    "quantity": 1,
                    "unitPrice": 250.0
                }
            ],
            "createdAt": "2025-02-18T01:26:07Z"
        }
        """;

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderId("ORD123456");
        orderRequest.setCreatedAt("2025-02-18T01:26:07Z");
        orderRequest.setCustomer(new Customer());
        orderRequest.setProducts(Arrays.asList(
                new Product("PROD001", "Teclado Gamer", 1, 250.0)
        ));
        when(objectMapper.readValue(message, OrderRequest.class)).thenReturn(orderRequest);

        when(orderRepository.existsByOrderId("ORD123456")).thenReturn(true);

        Exception exception = assertThrows(
                Exception.class,
                () -> orderConsumer.consumeOrderMessage(message)
        );

        assertEquals("Pedido duplicado detectado: ORD123456", exception.getMessage());
        verify(orderRepository, times(1)).existsByOrderId("ORD123456");
        verify(orderRepository, times(0)).save(orderCaptor.capture());
    }
}