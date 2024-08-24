package chassepoulet.simpleecommerceapijava.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfiguration {

    @Value("${stripe.api.key}")
    private String apiKey;

    @Bean
    public Stripe initStripe() {
        Stripe.apiKey = this.apiKey;
        return null;
    }
}
