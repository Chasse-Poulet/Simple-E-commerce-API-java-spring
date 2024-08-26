package chassepoulet.simpleecommerceapijava.repository;

import chassepoulet.simpleecommerceapijava.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {
    Optional<Order> findByPaymentPaymentIntentId(String paymentIntentId);
    List<Order> findAllByUserId(String userId);
    Optional<Order> findByUserIdAndId(String userId, String orderId);
}
