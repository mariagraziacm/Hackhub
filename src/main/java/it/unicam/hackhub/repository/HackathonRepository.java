package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.Hackathon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface HackathonRepository extends JpaRepository<Hackathon, String> {

    // save(), findAll() e findById(String) sono già inclusi automaticamente!

    /**
     
Verifica se esiste un hackathon con il nome specificato, ignorando maiuscole e minuscole.
Sostituisce il vecchio controllo in memoria stream/equalsIgnoreCase.*/
boolean existsByNameIgnoreCase(String name);
}