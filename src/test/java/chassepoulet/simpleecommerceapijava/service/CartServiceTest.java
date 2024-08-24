package chassepoulet.simpleecommerceapijava.service;

import chassepoulet.simpleecommerceapijava.model.Cart;
import chassepoulet.simpleecommerceapijava.model.CartItem;
import chassepoulet.simpleecommerceapijava.model.Product;
import chassepoulet.simpleecommerceapijava.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCartByUserId() {
        String userId = "id1234";

        Cart cart = new Cart();
        cart.setId(userId);

        Optional<Cart> oc = Optional.of(cart);

        when(cartRepository.findById(userId)).thenReturn(oc);

        Cart result = cartService.getCartByUserId(userId);

        verify(cartRepository).findById(userId);

        assertEquals(cart, result);
    }

    @Test
    void testSaveCart() {
        String userId = "id1234";

        Cart cart = new Cart();
        cart.setId(userId);

        when(cartRepository.save(cart)).thenReturn(cart);

        cartService.saveCart(cart);

        verify(cartRepository).save(cart);
    }

    @Test
    void testAddProductToCart() {
        String userId = "id1234";
        String productId = "book1234";
        int quantity = 2;

        Product book = new Product();
        book.setId(productId);
        book.setName("Book");
        book.setPrice(9.99);

        CartItem bookItem = new CartItem();
        bookItem.setProductId(productId);
        bookItem.setQuantity(quantity);

        Cart cart = new Cart();
        cart.setId(userId);

        Cart updatedCart = new Cart();
        updatedCart.setId(userId);
        updatedCart.setItems(List.of(bookItem));

        Optional<Cart> oc = Optional.of(cart);

        when(cartRepository.findById(userId)).thenReturn(oc);
        when(cartRepository.save(updatedCart)).thenReturn(updatedCart);

        Cart result = cartService.addProductToCart(userId, productId, quantity);

        verify(cartRepository).findById(userId);
        verify(cartRepository).save(updatedCart);

        assertEquals(updatedCart, result);
    }

    @Test
    void testRemoveProductFromCart() {
        String userId = "id1234";
        String productId = "book1234";

        Product book = new Product();
        book.setId(productId);
        book.setName("Book");
        book.setPrice(9.99);

        CartItem bookItem = new CartItem();
        bookItem.setProductId(productId);
        bookItem.setQuantity(2);

        Cart cart = new Cart();
        cart.setId(userId);
        cart.setItems(new ArrayList<>(List.of(bookItem)));

        Cart updatedCart = new Cart();
        updatedCart.setId(userId);

        Optional<Cart> oc = Optional.of(cart);

        when(cartRepository.findById(userId)).thenReturn(oc);
        when(cartRepository.save(updatedCart)).thenReturn(updatedCart);

        Cart result = cartService.removeProductFromCart(userId, productId);

        verify(cartRepository).findById(userId);
        verify(cartRepository).save(updatedCart);

        assertEquals(updatedCart, result);
    }

    @Test
    void testGetCartByPaymentIntentIdAndEmpty() {
        String userId = "id1234";
        String productId = "book1234";
        String paymentIntentId = "pi1234";

        Product book = new Product();
        book.setId(productId);
        book.setName("Book");
        book.setPrice(9.99);

        CartItem bookItem = new CartItem();
        bookItem.setProductId(productId);
        bookItem.setQuantity(2);

        Cart cart = new Cart();
        cart.setId(userId);
        cart.setPaymentIntentId(paymentIntentId);
        cart.setItems(new ArrayList<>(List.of(bookItem)));

        Cart updatedCart = new Cart();
        updatedCart.setId(userId);
        updatedCart.setPaymentIntentId(paymentIntentId);

        Optional<Cart> oc = Optional.of(cart);

        when(cartRepository.findByPaymentIntentId(paymentIntentId)).thenReturn(oc);
        when(cartRepository.save(updatedCart)).thenReturn(updatedCart);

        Cart result = cartService.getCartByPaymentIntentIdAndEmpty(paymentIntentId);

        verify(cartRepository).findByPaymentIntentId(paymentIntentId);
        verify(cartRepository).save(updatedCart);

        assertEquals(updatedCart, result);
    }
}
