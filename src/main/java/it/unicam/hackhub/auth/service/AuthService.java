package it.unicam.hackhub.auth.service;

import it.unicam.hackhub.auth.security.JwtUtil;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;

    // Iniettiamo sia il repository che la utility di JWT tramite Spring
    public AuthService(UserRepository userRepo, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public User register(String id, String username, String name, String surname, String email, String password) {

        // Verifica delegata in modo efficiente al database
        boolean exists = userRepo.existsByEmailOrUsername(email, username);

        if (exists) {
            throw new IllegalStateException("Email o username già registrati");
        }

        User user = new User(name, surname, username, email, password, id, null);
        userRepo.save(user);

        return user;
    }

    @Transactional(readOnly = true)
    public String login(String username, String password) {

        // Recupero mirato tramite query sul database
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User non trovato"));

        if (!user.getPassword().equals(password)) {
            throw new IllegalStateException("Password errata");
        }

        String role = "USER";

        return jwtUtil.generateToken(user.getId(), role);
    }

    public String validateToken(String token) {
        return jwtUtil.validateAndGetUserId(token);
    }
}