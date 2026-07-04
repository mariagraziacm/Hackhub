package it.unicam.hackhub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "calls")
public class Call {

    public enum CallStatus {
        RESERVED,
        WAITING_TEAM_RESPONSE,
        ACCEPTED,
        REJECTED
    }

    @Id
    private String id; // Rimosso final per JPA
    
    private String mentorId; // Rimosso final per JPA
    private String teamId; // Rimosso final per JPA
    private String hackathonId; // Rimosso final per JPA

    private String slotId;

    @Enumerated(EnumType.STRING) // Salva l'enum come testo nel DB
    private CallStatus status;

    // Costruttore vuoto obbligatorio per Spring Boot / JPA
    public Call() {
    }

    public Call(String id, String mentorId, String teamId, String hackathonId, String slotId) {
        this.id = id;
        this.mentorId = mentorId;
        this.teamId = teamId;
        this.hackathonId = hackathonId;
        this.slotId = slotId;
        this.status = CallStatus.RESERVED;
    }

    // --- GETTER ---
    public String getId() {
        return id;
    }

    public String getMentorId() {
        return mentorId;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getHackathonId() {
        return hackathonId;
    }

    public String getSlotId() {
        return slotId;
    }

    public CallStatus getStatus() {
        return status;
    }

    // --- SETTER (Utili per le operazioni di aggiornamento di JPA) ---
    public void setId(String id) { this.id = id; }
    public void setMentorId(String mentorId) { this.mentorId = mentorId; }
    public void setTeamId(String teamId) { this.teamId = teamId; }
    public void setHackathonId(String hackathonId) { this.hackathonId = hackathonId; }
    public void setSlotId(String slotId) { this.slotId = slotId; }
    public void setStatus(CallStatus status) { this.status = status; }

    // --- LA TUA LOGICA DI BUSINESS RIMANE INTATTA ---

    public void setWaitingResponse() {
        this.status = CallStatus.WAITING_TEAM_RESPONSE;
    }

    public void accept() {
        if (status != CallStatus.WAITING_TEAM_RESPONSE) {
            throw new IllegalStateException("Call non in stato accettabile");
        }
        this.status = CallStatus.ACCEPTED;
    }

    public void reject() {
        if (status != CallStatus.WAITING_TEAM_RESPONSE) {
            throw new IllegalStateException("Call non in stato rifiutabile");
        }
        this.status = CallStatus.REJECTED;
    }
}