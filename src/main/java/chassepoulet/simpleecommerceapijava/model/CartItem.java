package chassepoulet.simpleecommerceapijava.model;

import lombok.Data;

@Data
public class CartItem {
    private String productId;
    private int quantity;
}
