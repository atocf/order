package br.com.atocf.order.processor.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderStatusTest {

    @Test
    void testOrderStatusValues() {
        OrderStatus[] statuses = OrderStatus.values();

        assertEquals(2, statuses.length, "O enum OrderStatus deve conter 2 valores");
        assertEquals(OrderStatus.RECEIVED, statuses[0], "O primeiro valor deve ser 'RECEIVED'");
        assertEquals(OrderStatus.CALCULATED, statuses[1], "O segundo valor deve ser 'CALCULATED'");
    }

    @Test
    void testOrderStatusValueOf() {
        assertEquals(OrderStatus.RECEIVED, OrderStatus.valueOf("RECEIVED"), "Deve retornar 'RECEIVED'");
        assertEquals(OrderStatus.CALCULATED, OrderStatus.valueOf("CALCULATED"), "Deve retornar 'CALCULATED'");
    }
}