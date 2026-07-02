package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.Violation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ViolationRepository {
    private final List<Violation> violations = new ArrayList<>();

    public void save(Violation violation) {
        // Previene i duplicati rimuovendo la vecchia istanza prima di salvare quella aggiornata
        violations.removeIf(v -> v.getId().equals(violation.getId()));
        violations.add(violation);
    }

    public Optional<Violation> findById(String id) {
        return violations.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst();
    }

    // NUOVO: Ritorna la lista reale di tutte le violazioni salvate
    public List<Violation> findAll() {
        return new ArrayList<>(violations);
    }
}