package chassepoulet.simpleecommerceapijava.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "orders")
public class Order {
    @Id
    private String id;

    private String userId;
    private String paymentIntentId;
    private List<CartItem> items;
    private double totalAmount;
    private String status;
}
