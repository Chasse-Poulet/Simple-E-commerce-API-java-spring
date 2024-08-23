package chassepoulet.simpleecommerceapijava.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateProductDTO {

    @NotBlank(message = "The product must have a name")
    private String name;

    private String description;

    @NotNull(message = "The product must have a price")
    @Min(value = 0, message = "The price can't be negative")
    private Double price;
}
