package it.unicam.hackhub.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "violations")
public class Violation {
    
    // ENUM (Rimane identico)
    public enum ViolationStatus {
        PENDING, DISQUALIFY_TEAM, DISQUALIFY_MEMBER, NO_ACTION
    }

    @Id
    private String id; // Rimosso final per JPA
    
    private String hackathonId; // Rimosso final per JPA
    private String teamId; // Rimosso final per JPA
    private String reportedMemberId; // Rimosso final per JPA
    private String mentorId; // Rimosso final per JPA
    
    @Lob // Consente testi di motivazione molto lunghi nel DB
    private String reason; // Rimosso final per JPA

    // Inizializzazione con stato PENDING
    @Enumerated(EnumType.STRING) // Salva l'enum nel database come testo
    private ViolationStatus status = ViolationStatus.PENDING;

    // Costruttore vuoto obbligatorio per Spring Boot / JPA
    public Violation() {
    }

    public Violation(String id,
                     String hackathonId,
                     String teamId,
                     String reportedMemberId,
                     String mentorId,
                     String reason) {
        this.id = requireNotBlank(id, "id obbligatorio");
        this.hackathonId = requireNotBlank(hackathonId, "hackathonId obbligatorio");
        this.teamId = requireNotBlank(teamId, "teamId obbligatorio");
        this.reportedMemberId = reportedMemberId;
        this.mentorId = requireNotBlank(mentorId, "mentorId obbligatorio");
        this.reason = requireNotBlank(reason, "reason obbligatorio");
    }

    // --- GETTER ---
    public String getId() { return id; }
    public String getHackathonId() { return hackathonId; }
    public String getTeamId() { return teamId; }
    public String getReportedMemberId() { return reportedMemberId; }
    public String getMentorId() { return mentorId; }
    public String getReason() { return reason; }
    public ViolationStatus getStatus() { return status; }

    // --- SETTER (Utili per gli aggiornamenti di JPA) ---
    public void setId(String id) { this.id = id; }
    public void setHackathonId(String hackathonId) { this.hackathonId = hackathonId; }
    public void setTeamId(String teamId) { this.teamId = teamId; }
    public void setReportedMemberId(String reportedMemberId) { this.reportedMemberId = reportedMemberId; }
    public void setMentorId(String mentorId) { this.mentorId = mentorId; }
    public void setReason(String reason) { this.reason = reason; }
    public void setStatus(ViolationStatus status) { this.status = status; }

    // --- LA TUA LOGICA DI BUSINESS RIMANE INTATTA ---

    public boolean isPending() {
        return ViolationStatus.PENDING.equals(status);
    }

    public void setDisqualifyTeam() {
        ensurePending();
        this.status = ViolationStatus.DISQUALIFY_TEAM;
    }

    public void setDisqualifyMember() {
        ensurePending();
        this.status = ViolationStatus.DISQUALIFY_MEMBER;
    }

    public void setNoAction() {
        ensurePending();
        this.status = ViolationStatus.NO_ACTION;
    }

    private void ensurePending() {
        if (!isPending()) {
            throw new IllegalStateException("Violazione già gestita");
        }
    }

    private String requireNotBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Violation)) return false;
        Violation that = (Violation) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void resolve(ViolationStatus newStatus) {
        if (status != ViolationStatus.PENDING) {
            throw new IllegalStateException("Violazione già gestita");
        }

        if (newStatus == ViolationStatus.PENDING) {
            throw new IllegalArgumentException("Stato non valido");
        }

        this.status = newStatus;
    }
}