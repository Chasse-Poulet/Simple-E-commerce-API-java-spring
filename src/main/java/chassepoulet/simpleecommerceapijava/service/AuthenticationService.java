package chassepoulet.simpleecommerceapijava.service;

import chassepoulet.simpleecommerceapijava.dto.LoginUserDTO;
import chassepoulet.simpleecommerceapijava.dto.RegisterUserDTO;
import chassepoulet.simpleecommerceapijava.model.User;
import chassepoulet.simpleecommerceapijava.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User signup(RegisterUserDTO input) {
        long userCount = userRepository.count();

        User user = new User();
        user.setEmail(input.getEmail());
        user.setUsername(input.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(input.getPassword()));
        user.setFullName(input.getFullName());

        // The first user to sign up is the admin, then every user starts as a regular user
        user.setRoles(Set.of(userCount > 0 ? "ROLE_USER" : "ROLE_ADMIN"));

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        return userRepository.findByUsername(input.getUsername()).orElseThrow();
    }
}
