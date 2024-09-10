package chassepoulet.simpleecommerceapijava.service;

import chassepoulet.simpleecommerceapijava.model.*;
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
    void testGetOrder() {
        String userId = "id1234";
        String orderId = "order1234";

        Order order = new Order();
        order.setId(orderId);

        Optional<Order> oo = Optional.of(order);

        when(orderRepository.findByUserIdAndId(userId, orderId)).thenReturn(oo);

        Order result = orderService.getOrder(userId, orderId);

        verify(orderRepository).findByUserIdAndId(userId, orderId);

        assertEquals(order, result);
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
        cart.setItems(new ArrayList<>(List.of(bookItem)));

        double totalAmount = book.getPrice() * bookItem.getQuantity();

        Payment payment = new Payment();
        payment.setPaymentIntentId(paymentIntentId);
        payment.setStatus("PENDING");

        Order order = new Order();
        order.setUserId(userId);
        order.setPayment(payment);
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
    void testGetOrderByPaymentIntentIdAndUpdatePaymentStatus() {
        String orderId = "order1234";
        String paymentIntentId = "pi1234";

        Payment payment = new Payment();
        payment.setPaymentIntentId(paymentIntentId);
        payment.setStatus("PENDING");

        Payment updatedPayment = new Payment();
        updatedPayment.setPaymentIntentId(paymentIntentId);
        updatedPayment.setStatus("PAID");

        Order order = new Order();
        order.setId(orderId);
        order.setStatus("PENDING");
        order.setPayment(payment);

        Order updatedOrder = new Order();
        updatedOrder.setId(orderId);
        updatedOrder.setStatus("PAID");
        updatedOrder.setPayment(updatedPayment);

        Optional<Order> oo = Optional.of(order);

        when(orderRepository.findByPaymentPaymentIntentId(paymentIntentId)).thenReturn(oo);
        when(orderRepository.save(updatedOrder)).thenReturn(updatedOrder);
        doNothing().when(cartService).deleteCartByUserId(order.getUserId());

        orderService.getOrderByPaymentIntentIdAndUpdatePaymentStatus(paymentIntentId, updatedPayment.getStatus());

        verify(orderRepository).findByPaymentPaymentIntentId(paymentIntentId);
        verify(orderRepository).save(updatedOrder);
        verify(cartService).deleteCartByUserId(order.getUserId());

        assertEquals(updatedOrder, order);
    }
}
