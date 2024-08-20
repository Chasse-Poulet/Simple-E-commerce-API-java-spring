package chassepoulet.simpleecommerceapijava.service;

import chassepoulet.simpleecommerceapijava.model.User;
import chassepoulet.simpleecommerceapijava.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        return new ArrayList<>(userRepository.findAll());
    }
}
