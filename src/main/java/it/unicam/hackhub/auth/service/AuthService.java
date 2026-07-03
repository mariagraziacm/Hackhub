package it.unicam.hackhub.auth.service;


import it.unicam.hackhub.auth.security.JwtUtil;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.repository.UserRepository;


public class AuthService {

    private final UserRepository userRepo;
    private final JwtUtil jwtUtil = new JwtUtil();

    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User register(String id, String username, String name, String surname, String email, String password) {

        boolean exists = userRepo.findAll().stream()
                .anyMatch(u ->
                        u.getEmail().equals(email) ||
                                u.getUsername().equals(username)
                );

        if (exists) {
            throw new IllegalStateException("Email o username già registrati");
        }

        User user = new User(id, username, name, surname, email, password);
        userRepo.save(user);

        return user;
    }

    public String login(String username, String password) {

        User user = userRepo.findAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
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