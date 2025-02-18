package br.com.atocf.order.processor.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerTest {

    @Test
    void testCustomerSettersAndGetters() {
        Customer customer = new Customer();
        customer.setId("CUST001");
        customer.setName("Maria Silva");

        assertEquals("CUST001", customer.getId(), "O ID do cliente deve ser 'CUST001'");
        assertEquals("Maria Silva", customer.getName(), "O nome do cliente deve ser 'Maria Silva'");
    }
}