package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmailOrUsername(String email, String username);

    // Necessario per l'AuthService login
   

    // I metodi save(), findById() e findAll() sono già inclusi automaticamente da JpaRepository!

    /**
     
Spring Boot genera automaticamente la query SQL per questo metodo 
analizzando semplicemente il nome del metodo (Property Expression).*/
Optional<User> findByUsername(String username);
}