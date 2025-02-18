package br.com.atocf.order.processor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String productId;
    private String name;
    private Integer quantity;
    private Double unitPrice;
}
