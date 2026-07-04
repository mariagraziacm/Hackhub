package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.Call;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallRepository extends JpaRepository<Call, String> {
    // save(), findById() e findAll() sono già inclusi automaticamente da JpaRepository!
}