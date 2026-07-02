package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.Violation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ViolationRepository {
    private final List<Violation> violations = new ArrayList<>();

    public void save(Violation violation) {
        violations.add(violation);
    }

    public List<Violation> findAll() {
        return violations;
    }

    public Optional<Violation> findById(String id) {
        return violations.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst();
    }

    public List<Violation> findPending() {
        return violations.stream()
                .filter(Violation::isPending)
                .toList();
    }

    public void deleteById(String id) {
        violations.removeIf(v -> v.getId().equals(id));
    }
}
