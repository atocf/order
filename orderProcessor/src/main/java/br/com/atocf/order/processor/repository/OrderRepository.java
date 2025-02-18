package br.com.atocf.order.processor.repository;

import br.com.atocf.order.processor.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    boolean existsByOrderId(String orderId);
}