package chassepoulet.simpleecommerceapijava.controller;

import chassepoulet.simpleecommerceapijava.dto.LoginResponseDTO;
import chassepoulet.simpleecommerceapijava.dto.LoginUserDTO;
import chassepoulet.simpleecommerceapijava.dto.RegisterUserDTO;
import chassepoulet.simpleecommerceapijava.model.User;
import chassepoulet.simpleecommerceapijava.security.JwtUtil;
import chassepoulet.simpleecommerceapijava.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final JwtUtil jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtUtil jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDTO registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody LoginUserDTO loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponseDTO loginResponse = new LoginResponseDTO();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
