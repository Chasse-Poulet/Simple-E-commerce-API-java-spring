package chassepoulet.simpleecommerceapijava.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "payments")
public class Payment {

    @Id
    private String id;

    @NotBlank(message = "The payment must have a payment intent id")
    private String paymentIntentId;

    @NotBlank(message = "The payment must have a status")
    private String status;
}
