package br.com.atocf.order.query.repository;

import br.com.atocf.order.query.model.Order;
import br.com.atocf.order.query.model.OrderStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    List<Order> findByCustomer_Id(String customerId);

    List<Order> findByCreatedAtBetween(Date startDate, Date endDate);

    List<Order> findByStatus(OrderStatus status);
}