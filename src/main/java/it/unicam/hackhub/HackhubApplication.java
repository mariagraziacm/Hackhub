package it.unicam.hackhub;

import it.unicam.hackhub.model.*;
import it.unicam.hackhub.repository.*;
import it.unicam.hackhub.service.*;
import it.unicam.hackhub.state.InValutazioneState;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import it.unicam.hackhub.config.SecurityConfig;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SecurityConfig.class)
public class HackhubApplication {

    public static void main(String[] args) {
        SpringApplication.run(HackhubApplication.class, args);
    }
}
