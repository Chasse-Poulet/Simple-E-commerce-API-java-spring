package chassepoulet.simpleecommerceapijava.service;

import chassepoulet.simpleecommerceapijava.dto.LoginUserDTO;
import chassepoulet.simpleecommerceapijava.dto.RegisterUserDTO;
import chassepoulet.simpleecommerceapijava.model.User;
import chassepoulet.simpleecommerceapijava.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder bCryptPasswordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignup() {
        String password = "iamtheflash";
        String encodedPassword = "iamtheflashencoded";

        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setEmail("Flash@dc.com");
        registerUserDTO.setPassword(password);
        registerUserDTO.setUsername("Flash");
        registerUserDTO.setFullName("Barry Allen");

        User user = User.from(registerUserDTO);
        user.setPassword(encodedPassword);
        user.setRoles(Set.of("ROLE_ADMIN"));

        when(bCryptPasswordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.count()).thenReturn(0L);

        User result = authenticationService.signup(registerUserDTO);
        verify(userRepository).count();
        verify(bCryptPasswordEncoder).encode(password);
        verify(userRepository).save(user);
    }

    @Test
    void testLogin() {
        String password = "iamtheflash";
        String encodedPassword = "iamtheflashencoded";

        LoginUserDTO dto = new LoginUserDTO();
        dto.setUsername("Flash");
        dto.setPassword(password);

        User user = new User();
        user.setEmail("Flash@dc.com");
        user.setPassword(encodedPassword);
        user.setUsername("Flash");
        user.setFullName("Barry Allen");
        user.setRoles(Set.of("ROLE_ADMIN"));

        Optional<User> ou = Optional.of(user);

        when(userRepository.findByUsername(dto.getUsername())).thenReturn(ou);

        authenticationService.authenticate(dto);

        verify(userRepository).findByUsername(dto.getUsername());
    }
}
