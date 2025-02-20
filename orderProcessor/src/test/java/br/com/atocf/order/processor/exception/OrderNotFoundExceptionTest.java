package br.com.atocf.order.processor.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderNotFoundExceptionTest {

    @Test
    void testOrderNotFoundException() {
        String message = "Pedido não encontrado";
        OrderNotFoundException exception = new OrderNotFoundException(message);

        assertEquals(message, exception.getMessage(), "A mensagem da exceção deve ser igual à mensagem fornecida");
    }
}