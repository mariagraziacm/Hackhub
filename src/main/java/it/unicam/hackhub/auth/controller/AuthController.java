package it.unicam.hackhub.auth.controller;

import it.unicam.hackhub.model.User;
import it.unicam.hackhub.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /api/auth/register -> Registra un nuovo utente nel sistema
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterPayload payload) {
        try {
            User user = authService.register(
                    payload.getId(), 
                    payload.getUsername(), 
                    payload.getName(), 
                    payload.getSurname(), 
                    payload.getEmail(), 
                    payload.getPassword()
            );
            return ResponseEntity.ok("Registrazione completata: " + user.getUsername());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERRORE: " + e.getMessage());
        }
    }

    // POST /api/auth/login -> Autentica un utente e restituisce il token JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginPayload payload) {
        try {
            String token = authService.login(payload.getUsername(), payload.getPassword());
            // Restituiamo il token all'interno di un oggetto JSON
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERRORE: " + e.getMessage());
        }
    }

    // DTO per la registrazione
    public static class RegisterPayload {
        private String id;
        private String username;
        private String name;
        private String surname;
        private String email;
        private String password;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getSurname() { return surname; }
        public void setSurname(String surname) { this.surname = surname; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // DTO per il login
    public static class LoginPayload {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}