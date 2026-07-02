package it.unicam.hackhub.service;

import it.unicam.hackhub.model.Violation;
import it.unicam.hackhub.model.Violation.ViolationStatus;
import it.unicam.hackhub.repository.ViolationRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ViolationService {
    private final ViolationRepository repo;

    public ViolationService(ViolationRepository repo) {
        this.repo = repo;
    }

    public Violation createViolation(String hackathonId, String teamId, String reportedMemberId, String mentorId, String reason) {
        Violation violation = new Violation(
                UUID.randomUUID().toString(),
                hackathonId,
                teamId,
                reportedMemberId,
                mentorId,
                reason
        );
        repo.save(violation);
        return violation;
    }

    public Violation getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Violazione non trovata"));
    }

    // Ritorna tutte le violazioni ancora in stato PENDING
    public List<Violation> listPendingViolations() {
        return repo.findAll().stream()
                .filter(Violation::isPending)
                .collect(Collectors.toList());
    }

    // Gestisce la squalifica del Team
    public void handleDisqualifyTeam(String violationId) {
        Violation violation = getById(violationId);
        violation.setDisqualifyTeam(); // Chiama il metodo corretto del modello
        repo.save(violation);
    }

    // Gestisce la squalifica del singolo Membro
    public void handleDisqualifyMember(String violationId) {
        Violation violation = getById(violationId);
        violation.setDisqualifyMember(); // Chiama il metodo corretto del modello
        repo.save(violation);
    }

    // Archivia senza nessuna azione
    public void handleNoAction(String violationId) {
        Violation violation = getById(violationId);
        violation.setNoAction(); // Chiama il metodo corretto del modello
        repo.save(violation);
    }
}