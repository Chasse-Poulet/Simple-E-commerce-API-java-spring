package chassepoulet.simpleecommerceapijava.service;

import chassepoulet.simpleecommerceapijava.model.Cart;
import chassepoulet.simpleecommerceapijava.model.CartItem;
import chassepoulet.simpleecommerceapijava.model.User;
import chassepoulet.simpleecommerceapijava.repository.CartRepository;
import chassepoulet.simpleecommerceapijava.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    public Cart getCartByUserId(String userId) {
        return cartRepository.findById(userId).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setId(userId);

            cartRepository.save(cart);

            User user = userRepository.findById(userId).orElseThrow();
            user.setCart(cart);

            userRepository.save(user);

            return cart;
        });
    }

    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }

    public Cart addProductToCart(String userId, String productId, int quantity) {
        Cart cart = getCartByUserId(userId);

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            // Update quantity if the product already exists in the cart
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            // Add new item to the cart
            CartItem newItem = new CartItem();
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
    }

    public Cart removeProductFromCart(String userId, String productId) {
        Cart cart = getCartByUserId(userId);

        cart.getItems().removeIf(item -> item.getProductId().equals(productId));

        return cartRepository.save(cart);
    }

    public void deleteCartByUserId(String userId) {
        cartRepository.deleteById(userId);
    }
}
