package chassepoulet.simpleecommerceapijava.repository;

import chassepoulet.simpleecommerceapijava.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, String> {
}
