package it.unicam.hackhub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "team_members")
public class TeamMember {

    @Id
    private String id; // Rimosso final per JPA

    // Molti TeamMember fanno riferimento a un solo User.
    // Usiamo FetchType.EAGER perché quando carichiamo un membro vogliamo quasi sempre sapere chi è l'utente associato.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false) // Chiave esterna verso la tabella degli utenti
    private User user; // Rimosso final per JPA

    @Enumerated(EnumType.STRING) // Salva l'enum nel database come testo ("LEADER", "MEMBER")
    private Role role; // Rimosso final per JPA

    // ENUM (Rimane identico)
    public enum Role {
        LEADER,
        MEMBER
    }

    // Costruttore vuoto obbligatorio per JPA
    public TeamMember() {
    }

    public TeamMember(String id, User user, Role role) {
        this.id = id;
        this.user = user;
        this.role = role;
    }

    public String getId() { return id; }
    public User getUser() { return user; }
    public Role getRole() { return role; }

    public String getUserId() {
        return user.getId();
    }

    public boolean isLeader() {
        return role == Role.LEADER;
    }
}