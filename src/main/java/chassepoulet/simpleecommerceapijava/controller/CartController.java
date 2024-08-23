package chassepoulet.simpleecommerceapijava.controller;

import chassepoulet.simpleecommerceapijava.dto.AddProductToCartDTO;
import chassepoulet.simpleecommerceapijava.dto.RemoveProductFromCartDTO;
import chassepoulet.simpleecommerceapijava.model.Cart;
import chassepoulet.simpleecommerceapijava.model.Order;
import chassepoulet.simpleecommerceapijava.service.CartService;
import chassepoulet.simpleecommerceapijava.service.OrderService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/cart")
@SecurityRequirement(name = "Bearer Authentication")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    @PreAuthorize("#userId == principal.id")
    public Cart getCart(@PathVariable String userId) {
        return cartService.getCartByUserId(userId);
    }

    @PostMapping("/add")
    @PreAuthorize("#userId == principal.id")
    public Cart addProductToCart(
            @PathVariable String userId,
            @RequestBody AddProductToCartDTO addToCartDTO) {

        return cartService.addProductToCart(userId, addToCartDTO.getProductId(), addToCartDTO.getQuantity());
    }

    @DeleteMapping("/remove")
    @PreAuthorize("#userId == principal.id")
    public Cart removeProductFromCart(
            @PathVariable String userId,
            @RequestBody RemoveProductFromCartDTO removeProductFromCartDTO) {

        return cartService.removeProductFromCart(userId, removeProductFromCartDTO.getProductId());
    }

    @PostMapping("/checkout")
    @PreAuthorize("#userId == principal.id")
    public String createOrder(@PathVariable String userId) throws StripeException {
        return orderService.createOrder(userId);
    }
}
