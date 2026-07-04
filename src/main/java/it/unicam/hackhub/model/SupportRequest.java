package it.unicam.hackhub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "support_requests")
public class SupportRequest {

    // ENUM (Rimane identico)
    public enum SupportStatus {
        PENDING,
        ANSWERED
    }

    @Id
    private String id; // Rimosso final per JPA
    
    private String hackathonId; // Rimosso final per JPA
    private String teamId; // Rimosso final per JPA
    private String mentorId; // Rimosso final per JPA

    @Lob // Supporta messaggi di richiesta di supporto molto lunghi
    private String message; // Rimosso final per JPA
    
    private String callId;

    @Enumerated(EnumType.STRING) // Salva l'enum come testo nel DB
    private SupportStatus status = SupportStatus.PENDING;

    // Costruttore vuoto obbligatorio per Spring Boot / JPA
    public SupportRequest() {
    }

    public SupportRequest(String id,
                          String hackathonId,
                          String teamId,
                          String mentorId,
                          String message) {
        this.id = id;
        this.hackathonId = hackathonId;
        this.teamId = teamId;
        this.mentorId = mentorId;
        this.message = message;
    }

    // --- GETTER ---
    public String getId() { return id; }
    public String getHackathonId() { return hackathonId; }
    public String getTeamId() { return teamId; }
    public String getMentorId() { return mentorId; }
    public String getMessage() { return message; }
    public SupportStatus getStatus() { return status; }
    public String getCallId() { return callId; }

    // --- SETTER (Utili per JPA ed evoluzioni future) ---
    public void setId(String id) { this.id = id; }
    public void setHackathonId(String hackathonId) { this.hackathonId = hackathonId; }
    public void setTeamId(String teamId) { this.teamId = teamId; }
    public void setMentorId(String mentorId) { this.mentorId = mentorId; }
    public void setMessage(String message) { this.message = message; }
    public void setStatus(SupportStatus status) { this.status = status; }
    public void setCallId(String callId) { this.callId = callId; }

    // --- LA TUA LOGICA DI BUSINESS RIMANE INTATTA ---

    public void markAnswered() {
        this.status = SupportStatus.ANSWERED;
    }
}