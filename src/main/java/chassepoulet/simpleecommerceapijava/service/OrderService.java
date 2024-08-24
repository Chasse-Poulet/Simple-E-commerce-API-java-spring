package chassepoulet.simpleecommerceapijava.service;

import chassepoulet.simpleecommerceapijava.exception.ProductNotFoundException;
import chassepoulet.simpleecommerceapijava.model.Cart;
import chassepoulet.simpleecommerceapijava.model.Order;
import chassepoulet.simpleecommerceapijava.repository.OrderRepository;
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

    public List<Order> getAllOrdersByUserId(String userId) {
        return orderRepository.findAllByUserId(userId);
    }

    public String createOrder(String userId) throws StripeException {
        Cart cart = cartService.getCartByUserId(userId);

        double totalAmount = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * getProductPrice(item.getProductId()))
                .sum();

        PaymentIntent paymentIntent = stripeService.createPaymentIntent(totalAmount);

        Order order = new Order();
        order.setUserId(userId);
        order.setPaymentIntentId(paymentIntent.getId());
        order.setItems(cart.getItems());
        order.setTotalAmount(totalAmount);
        order.setStatus("PENDING");

        cart.setPaymentIntentId(paymentIntent.getId());

        orderRepository.save(order);
        cartService.saveCart(cart);

        return paymentIntent.getId();
    }

    public Order getOrderByPaymentIntentIdAndPay(String paymentIntentId) {
        Order order = orderRepository.findByPaymentIntentId(paymentIntentId).orElse(null);
        if (order != null) {
            order.setStatus("PAID");
            orderRepository.save(order);
        }
        return order;
    }

    private double getProductPrice(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId))
                .getPrice();
    }
}
