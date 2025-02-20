package br.com.atocf.order.processor.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderTest {

    @Test
    void testOrderSettersAndGetters() {
        Customer customer = new Customer();
        customer.setId("CUST001");
        customer.setName("Maria Silva");

        Product product1 = new Product();
        product1.setProductId("PROD001");
        product1.setName("Teclado Gamer");
        product1.setQuantity(1);
        product1.setUnitPrice(250.0);

        Product product2 = new Product();
        product2.setProductId("PROD002");
        product2.setName("Mouse Gamer");
        product2.setQuantity(2);
        product2.setUnitPrice(120.0);

        Order order = new Order();
        order.setOrderId("ORD123456");
        order.setCustomer(customer);
        order.setProducts(Arrays.asList(product1, product2));
        order.setCreatedAt(new Date());
        order.setEntryAt(new Date());
        order.setUpdatedAt(new Date());

        assertEquals("ORD123456", order.getOrderId(), "O ID do pedido deve ser 'ORD123456'");
        assertEquals(customer, order.getCustomer(), "O cliente deve ser 'Maria Silva'");
        assertEquals(2, order.getProducts().size(), "O pedido deve conter 2 produtos");
    }

    @Test
    void testCalculateTotalValue() {
        Product product1 = new Product();
        product1.setProductId("PROD001");
        product1.setName("Teclado Gamer");
        product1.setQuantity(1);
        product1.setUnitPrice(250.0);

        Product product2 = new Product();
        product2.setProductId("PROD002");
        product2.setName("Mouse Gamer");
        product2.setQuantity(2);
        product2.setUnitPrice(120.0);

        Order order = new Order();
        order.setProducts(Arrays.asList(product1, product2));

        order.calculateTotalValue();

        assertEquals(490.0, order.getTotalValue(), "O valor total do pedido deve ser 490.0");
        assertEquals(OrderStatus.CALCULATED, order.getStatus(), "O status do pedido deve ser 'CALCULATED'");
        assertNotNull(order.getUpdatedAt(), "A data de atualização deve ser preenchida");
    }
}