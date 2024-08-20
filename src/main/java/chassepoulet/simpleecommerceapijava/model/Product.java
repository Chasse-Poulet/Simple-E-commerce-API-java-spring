package chassepoulet.simpleecommerceapijava.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "products")
public class Product {
    @Id
    private String id;

    @NotBlank(message = "The product must have a name")
    private String name;

    private String description;

    @NotNull(message = "The product must have a price")
    @Min(value = 0, message = "The price can't be negative")
    private Double price;
}
