package chassepoulet.simpleecommerceapijava.service;

import chassepoulet.simpleecommerceapijava.exception.ProductNotFoundException;
import chassepoulet.simpleecommerceapijava.model.Cart;
import chassepoulet.simpleecommerceapijava.model.Order;
import chassepoulet.simpleecommerceapijava.model.Product;
import chassepoulet.simpleecommerceapijava.repository.OrderRepository;
import chassepoulet.simpleecommerceapijava.repository.ProductRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public String createOrder(String userId) throws StripeException {
        Cart cart = cartService.getCartByUserId(userId);

        double totalAmount = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * getProductPrice(item.getProductId()))
                .sum();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) totalAmount * 100)
                .setCurrency("eur")
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

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

    public Order getProductByPaymentIntentIdAndPay(String paymentIntentId) {
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
