package chassepoulet.simpleecommerceapijava.model;

import chassepoulet.simpleecommerceapijava.dto.RegisterUserDTO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Document(collection = "users")
public class User implements UserDetails {
    @Id
    private String id;

    private String email;
    private String username;
    private String password;
    private String fullName;
    private Set<String> roles;

    private Cart cart;

    public static User from(RegisterUserDTO dto) {
        User u = new User();
        u.setEmail(dto.getEmail());
        u.setPassword(dto.getPassword());
        u.setUsername(dto.getUsername());
        u.setFullName(dto.getFullName());
        return u;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
