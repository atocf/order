package br.com.atocf.order.query.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    @Indexed(unique = true)
    private String orderId;

    private Customer customer;
    private List<Product> products;
    private Date createdAt;
    private Date entryAt;
    private Date updatedAt;
    private Double totalValue;
    private OrderStatus status;
}