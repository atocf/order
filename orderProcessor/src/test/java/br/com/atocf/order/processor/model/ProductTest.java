package br.com.atocf.order.processor.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductTest {

    @Test
    void testProductSettersAndGetters() {
        Product product = new Product();
        product.setProductId("PROD001");
        product.setName("Teclado Gamer");
        product.setQuantity(2);
        product.setUnitPrice(250.0);

        assertEquals("PROD001", product.getProductId(), "O ID do produto deve ser 'PROD001'");
        assertEquals("Teclado Gamer", product.getName(), "O nome do produto deve ser 'Teclado Gamer'");
        assertEquals(2, product.getQuantity(), "A quantidade do produto deve ser 2");
        assertEquals(250.0, product.getUnitPrice(), "O preço unitário do produto deve ser 250.0");
    }
}