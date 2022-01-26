package hexlet.code.app.service.impl;

import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.TokenService;
import hexlet.code.app.service.UserAuthenticationService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class TokenAuthenticationService implements UserAuthenticationService {

    private final TokenService tokenService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public TokenAuthenticationService(TokenService tokenService,
                                      UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String login(String username, String password) {
        return userRepository.findUserByEmail(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> tokenService.expiring(Map.of("username", username)))
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
    }

    @Override
    public Optional<User> findByToken(String token) {
        return userRepository.findUserByEmail(tokenService.verify(token).get("username").toString());
    }
}