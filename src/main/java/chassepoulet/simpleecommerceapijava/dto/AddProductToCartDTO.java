package chassepoulet.simpleecommerceapijava.dto;

import lombok.Data;

@Data
public class AddProductToCartDTO {
    private String productId;
    private int quantity;
}
