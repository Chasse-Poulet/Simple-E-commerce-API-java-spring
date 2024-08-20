package chassepoulet.simpleecommerceapijava.controller;

import chassepoulet.simpleecommerceapijava.dto.LoginResponseDto;
import chassepoulet.simpleecommerceapijava.dto.LoginUserDto;
import chassepoulet.simpleecommerceapijava.dto.RegisterUserDto;
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
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponseDto loginResponse = new LoginResponseDto();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
