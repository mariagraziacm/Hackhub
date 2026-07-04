package it.unicam.hackhub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "staff_members")
@Inheritance(strategy = InheritanceType.JOINED) // Strategia a tabelle collegate per l'ereditarietà
public abstract class StaffMember {

    @Id
    private String id; // Rimosso final per JPA

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Rimosso final per JPA

    private String hackathonId; // Rimosso final per JPA

    // Costruttore vuoto obbligatorio per JPA nelle classi astratte (protected va benissimo)
    protected StaffMember() {
    }

    public StaffMember(String id, User user, String hackathonId) {
        this.id = id;
        this.user = user;
        this.hackathonId = hackathonId;
    }

    // --- GETTER ---
    public String getId() { return id; }
    public User getUser() { return user; }
    public String getHackathonId() { return hackathonId; }

    // --- SETTER (Utili per JPA) ---
    public void setId(String id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setHackathonId(String hackathonId) { this.hackathonId = hackathonId; }
}