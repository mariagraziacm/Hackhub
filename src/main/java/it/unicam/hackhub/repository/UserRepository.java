package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmailOrUsername(String email, String username);

Optional<User> findByUsername(String username);
}