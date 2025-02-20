package br.com.atocf.order.processor.dto;


import br.com.atocf.order.processor.model.Customer;
import br.com.atocf.order.processor.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private String orderId;
    private Customer customer;
    private List<Product> products;
    private String createdAt;
}
