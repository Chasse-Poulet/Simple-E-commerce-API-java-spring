package chassepoulet.simpleecommerceapijava.repository;

import chassepoulet.simpleecommerceapijava.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {
    Optional<Order> findByPaymentIntentId(String paymentIntentId);
}
