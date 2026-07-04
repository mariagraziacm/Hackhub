package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.Violation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViolationRepository extends JpaRepository<Violation, String> {

    // Tutti i tuoi metodi precedenti sono già inclusi automaticamente:
    // - save(Violation) gestisce sia l'inserimento che l'aggiornamento.
    // - findById(String) cerca per ID restituendo un Optional.
    // - findAll() restituisce la lista completa delle violazioni presenti nel database.
    // - deleteById(String) sostituisce il vecchio metodo delete(String) per rimuovere i record.
}
