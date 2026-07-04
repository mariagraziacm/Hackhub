package it.unicam.hackhub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

public class App {
    @Controller
public class AppController {

    @GetMapping("/login") // Questo deve corrispondere all'URL
    public String loginPage() {
        return "login"; // Questo deve corrispondere al nome del file in /templates/
    }
}
    
}
