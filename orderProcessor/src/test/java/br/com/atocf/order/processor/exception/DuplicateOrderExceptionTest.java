package br.com.atocf.order.processor.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DuplicateOrderExceptionTest {

    @Test
    void testDuplicateOrderException() {
        String message = "Pedido duplicado";
        DuplicateOrderException exception = new DuplicateOrderException(message);

        assertEquals(message, exception.getMessage(), "A mensagem da exceção deve ser igual à mensagem fornecida");
    }
}