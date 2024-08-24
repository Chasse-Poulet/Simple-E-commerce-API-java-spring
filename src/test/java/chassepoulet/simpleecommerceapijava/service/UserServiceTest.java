package chassepoulet.simpleecommerceapijava.service;

import chassepoulet.simpleecommerceapijava.model.User;
import chassepoulet.simpleecommerceapijava.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAllUsers() {
        User flash = new User();
        flash.setUsername("Flash");

        User batman = new User();
        batman.setUsername("Batman");

        List<User> users = List.of(flash, batman);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.allUsers();

        verify(userRepository).findAll();

        assertIterableEquals(users, result);
    }
}
