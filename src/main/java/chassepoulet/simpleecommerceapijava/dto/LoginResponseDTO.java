package chassepoulet.simpleecommerceapijava.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private String token;
    private long expiresIn;
}
