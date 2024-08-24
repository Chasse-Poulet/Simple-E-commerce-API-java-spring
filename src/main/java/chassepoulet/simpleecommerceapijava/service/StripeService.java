package chassepoulet.simpleecommerceapijava.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripeService {
    public PaymentIntent createPaymentIntent(double totalAmount) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (totalAmount * 100))
                .setCurrency("eur")
                .build();

        return PaymentIntent.create(params);
    }
}
