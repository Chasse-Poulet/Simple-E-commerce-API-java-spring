package chassepoulet.simpleecommerceapijava.controller;

import chassepoulet.simpleecommerceapijava.model.Cart;
import chassepoulet.simpleecommerceapijava.model.Order;
import chassepoulet.simpleecommerceapijava.service.CartService;
import chassepoulet.simpleecommerceapijava.service.OrderService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stripe")
public class StripeWebhookController {

    @Value("${stripe.endpoint.secret}")
    private static String ENDPOINT_SECRET;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @PostMapping("/webhook")
    public void handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) throws Exception {

        try {
            Event event = Webhook.constructEvent(payload, sigHeader, ENDPOINT_SECRET);

            if (event.getType().equals("payment_intent.succeeded")) {
                PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);

                if (paymentIntent != null) {
                    String paymentIntentId = paymentIntent.getId();
                    Order order = orderService.getProductByPaymentIntentIdAndPay(paymentIntentId);
                    Cart cart = cartService.getCartByPaymentIntentIdAndEmpty(paymentIntentId);
                }
            }
        } catch (SignatureVerificationException e) {
            throw new RuntimeException("Invalid signature");
        }
    }
}
