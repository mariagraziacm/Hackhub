package it.unicam.hackhub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "participation_requests")
public class ParticipationRequest {

    // ENUM (Rimane identico)
    public enum ParticipationRequestState {
        PENDING,
        ACCEPTED,
        DECLINED
    }

    @Id
    private String id; // Rimosso final per JPA

    // Molte richieste possono appartenere allo stesso utente
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Rimosso final per JPA

    // Molte richieste possono essere dirette allo stesso team
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team; // Rimosso final per JPA

    @Enumerated(EnumType.STRING) // Salva l'enum come testo nel database
    private ParticipationRequestState state;

    // Costruttore vuoto obbligatorio per Spring Boot / JPA
    public ParticipationRequest() {
    }

    public ParticipationRequest(String id, User user, Team team) {
        this.id = id;
        this.user = user;
        this.team = team;
        this.state = ParticipationRequestState.PENDING;
    }

    // --- GETTER ---
    public String getId() { return id; }
    public User getUser() { return user; }
    public Team getTeam() { return team; }
    public ParticipationRequestState getState() { return state; }

    // --- SETTER (Utili per JPA) ---
    public void setId(String id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setTeam(Team team) { this.team = team; }
    public void setState(ParticipationRequestState state) { this.state = state; }

    // --- LA TUA LOGICA DI BUSINESS RIMANE INTATTA ---

    public void accept() {
        if (state != ParticipationRequestState.PENDING)
            throw new IllegalStateException("Already processed");
        state = ParticipationRequestState.ACCEPTED;
    }

    public void decline() {
        if (state != ParticipationRequestState.PENDING)
            throw new IllegalStateException("Already processed");
        state = ParticipationRequestState.DECLINED;
    }
}