package br.com.atocf.order.processor.dto;

import br.com.atocf.order.processor.model.Customer;
import br.com.atocf.order.processor.model.Product;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderRequestTest {

    @Test
    void testOrderRequestCreation() {
        String orderId = "12345";
        Customer customer = new Customer();
        customer.setName("João da Silva");

        Product product1 = new Product();
        product1.setProductId("p1");
        product1.setName("Produto 1");
        product1.setQuantity(2);
        product1.setUnitPrice(100.0);

        Product product2 = new Product();
        product2.setProductId("p2");
        product2.setName("Produto 2");
        product1.setQuantity(4);
        product2.setUnitPrice(50.0);

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        String createdAt = "sistema-teste";

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderId(orderId);
        orderRequest.setCustomer(customer);
        orderRequest.setProducts(products);
        orderRequest.setCreatedAt(createdAt);

        assertEquals(orderId, orderRequest.getOrderId(), "O ID do pedido deve ser atribuído corretamente");
        assertEquals(customer, orderRequest.getCustomer(), "O cliente deve ser atribuído corretamente");
        assertEquals(products, orderRequest.getProducts(), "Os produtos devem ser atribuídos corretamente");
        assertEquals(createdAt, orderRequest.getCreatedAt(), "O campo createdBy deve ser atribuído corretamente");
    }

    @Test
    void testOrderRequestWithEmptyProducts() {
        Customer customer = new Customer();
        customer.setName("João da Silva");

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomer(customer);
        orderRequest.setProducts(new ArrayList<>());

        assertNotNull(orderRequest.getProducts(), "A lista de produtos não deve ser nula");
        assertTrue(orderRequest.getProducts().isEmpty(), "A lista de produtos deve estar vazia");
    }

    @Test
    void testOrderRequestWithNullValues() {
        OrderRequest orderRequest = new OrderRequest();

        assertNull(orderRequest.getOrderId(), "O ID do pedido deve ser nulo por padrão");
        assertNull(orderRequest.getCustomer(), "O cliente deve ser nulo por padrão");
        assertNull(orderRequest.getProducts(), "A lista de produtos deve ser nula por padrão");
        assertNull(orderRequest.getCreatedAt(), "O campo createdBy deve ser nulo por padrão");
    }
}