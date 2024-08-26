package chassepoulet.simpleecommerceapijava.repository;

import chassepoulet.simpleecommerceapijava.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart, String> {
}
