package chassepoulet.simpleecommerceapijava.service;

import chassepoulet.simpleecommerceapijava.exception.ProductNotFoundException;
import chassepoulet.simpleecommerceapijava.model.Cart;
import chassepoulet.simpleecommerceapijava.model.Order;
import chassepoulet.simpleecommerceapijava.model.Payment;
import chassepoulet.simpleecommerceapijava.repository.OrderRepository;
import chassepoulet.simpleecommerceapijava.repository.PaymentRepository;
import chassepoulet.simpleecommerceapijava.repository.ProductRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private CartService cartService;

    @Autowired
    private StripeService stripeService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public List<Order> getAllOrdersByUserId(String userId) {
        return orderRepository.findAllByUserId(userId);
    }

    public Order getOrder(String userId, String orderId) {
        return orderRepository.findByUserIdAndId(userId, orderId).orElse(null);
    }

    public String createOrder(String userId) throws StripeException {
        Cart cart = cartService.getCartByUserId(userId);

        double totalAmount = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * getProductPrice(item.getProductId()))
                .sum();

        PaymentIntent paymentIntent = stripeService.createPaymentIntent(totalAmount);
        String paymentIntentId = paymentIntent.getId();

        Payment payment = new Payment();
        payment.setPaymentIntentId(paymentIntentId);
        payment.setStatus(paymentIntent.getStatus());

        paymentRepository.save(payment);

        Order order = new Order();
        order.setUserId(userId);
        order.setPayment(payment);
        order.setItems(cart.getItems());
        order.setTotalAmount(totalAmount);
        order.setStatus("PENDING");
        order.setCurrency("eur");

        orderRepository.save(order);

        return paymentIntent.getId();
    }

    public void getOrderByPaymentIntentIdAndUpdatePaymentStatus(String paymentIntentId, String paymentIntentStatus) {
        Order order = orderRepository.findByPaymentPaymentIntentId(paymentIntentId).orElse(null);
        if (order != null) {
            order.setStatus("PAID");
            order.getPayment().setStatus(paymentIntentStatus);
            orderRepository.save(order);
            cartService.deleteCartByUserId(order.getUserId());
        }
    }

    private double getProductPrice(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId))
                .getPrice();
    }
}
