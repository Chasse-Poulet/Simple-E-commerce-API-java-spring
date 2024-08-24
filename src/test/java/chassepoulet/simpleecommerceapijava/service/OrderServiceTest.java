package chassepoulet.simpleecommerceapijava.service;

import chassepoulet.simpleecommerceapijava.model.Cart;
import chassepoulet.simpleecommerceapijava.model.CartItem;
import chassepoulet.simpleecommerceapijava.model.Order;
import chassepoulet.simpleecommerceapijava.model.Product;
import chassepoulet.simpleecommerceapijava.repository.OrderRepository;
import chassepoulet.simpleecommerceapijava.repository.ProductRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private CartService cartService;

    @Mock
    private StripeService stripeService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllOrders() {
        String userId = "id1234";
        String orderId = "order1234";
        String order2Id = "order5678";

        Order order = new Order();
        order.setId(orderId);

        Order order2 = new Order();
        order2.setId(order2Id);

        List<Order> orders = List.of(order, order2);

        when(orderRepository.findAllByUserId(userId)).thenReturn(orders);

        List<Order> result = orderService.getAllOrdersByUserId(userId);

        verify(orderRepository).findAllByUserId(userId);

        assertIterableEquals(orders, result);
    }

    @Test
    void testCreateOrder() {
        String userId = "id1234";
        String productId = "book1234";
        String paymentIntentId = "pi1234";

        PaymentIntent intent = new PaymentIntent();
        intent.setId(paymentIntentId);

        Product book = new Product();
        book.setId(productId);
        book.setName("Book");
        book.setPrice(9.99);

        Optional<Product> op = Optional.of(book);

        CartItem bookItem = new CartItem();
        bookItem.setProductId(productId);
        bookItem.setQuantity(2);

        Cart cart = new Cart();
        cart.setId(userId);
        cart.setPaymentIntentId(paymentIntentId);
        cart.setItems(new ArrayList<>(List.of(bookItem)));

        double totalAmount = book.getPrice() * bookItem.getQuantity();

        Order order = new Order();
        order.setUserId(userId);
        order.setPaymentIntentId(paymentIntentId);
        order.setItems(cart.getItems());
        order.setTotalAmount(totalAmount);
        order.setStatus("PENDING");

        try {
            when(cartService.getCartByUserId(userId)).thenReturn(cart);
            when(productRepository.findById(productId)).thenReturn(op);
            when(stripeService.createPaymentIntent(totalAmount)).thenReturn(intent);
            when(orderRepository.save(order)).thenReturn(order);
            doNothing().when(cartService).saveCart(cart);

            String result = orderService.createOrder(userId);

            verify(cartService).getCartByUserId(userId);
            verify(productRepository).findById(productId);
            verify(stripeService).createPaymentIntent(totalAmount);
            verify(orderRepository).save(order);
            verify(cartService).saveCart(cart);

            assertEquals(paymentIntentId, result);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetProductByPaymentIntentIdAndPay() {
        String orderId = "order1234";
        String paymentIntentId = "pi1234";

        Order order = new Order();
        order.setId(orderId);
        order.setStatus("PENDING");
        order.setPaymentIntentId(paymentIntentId);

        Order updatedOrder = new Order();
        updatedOrder.setId(orderId);
        updatedOrder.setStatus("PAID");
        updatedOrder.setPaymentIntentId(paymentIntentId);

        Optional<Order> oo = Optional.of(order);

        when(orderRepository.findByPaymentIntentId(paymentIntentId)).thenReturn(oo);
        when(orderRepository.save(updatedOrder)).thenReturn(updatedOrder);

        Order result = orderService.getOrderByPaymentIntentIdAndPay(paymentIntentId);

        verify(orderRepository).findByPaymentIntentId(paymentIntentId);
        verify(orderRepository).save(updatedOrder);

        assertEquals(updatedOrder, order);
    }
}
