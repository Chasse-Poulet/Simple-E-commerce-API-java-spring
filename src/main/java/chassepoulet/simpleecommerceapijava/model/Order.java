package chassepoulet.simpleecommerceapijava.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Data
@Document(collection = "orders")
public class Order {
    @Id
    private String id;

    private String userId;
    private String status;
    private String currency;
    private double totalAmount;
    private List<CartItem> items;

    @DocumentReference
    private Payment payment;
}
