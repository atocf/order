package br.com.atocf.order.processor.exception;

public class DuplicateOrderException extends RuntimeException {

    public DuplicateOrderException(String message) {
        super(message);
    }
}