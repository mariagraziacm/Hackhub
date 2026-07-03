package it.unicam.hackhub.auth.controller;

import it.unicam.hackhub.model.User;
import it.unicam.hackhub.auth.service.AuthService;

public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public void register(String id, String username, String name, String surname, String email, String password) {
        try {
            User user = authService.register(id, username, name, surname, email, password);
            System.out.println("Registrazione completata: " + user.getUsername());
        } catch (Exception e) {
            System.out.println("ERRORE: " + e.getMessage());
        }
    }

    public void login(String username, String password) {
        try {
            String token = authService.login(username, password);
            System.out.println("TOKEN: " + token);
        } catch (Exception e) {
            System.out.println("ERRORE: " + e.getMessage());
        }
    }
}