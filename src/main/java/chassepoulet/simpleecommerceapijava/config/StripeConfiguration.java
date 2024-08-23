package chassepoulet.simpleecommerceapijava.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfiguration {

    @Value("${stripe.api.key}")
    private static String API_KEY;

    public StripeConfiguration() {
        Stripe.apiKey = API_KEY;
    }
}
