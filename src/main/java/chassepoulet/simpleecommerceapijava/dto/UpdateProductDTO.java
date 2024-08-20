package chassepoulet.simpleecommerceapijava.dto;

import lombok.Data;

@Data
public class UpdateProductDTO {
    private String name;
    private String description;
    private Double price;
}
