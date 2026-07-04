package it.unicam.hackhub.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    private String id; // Rimosso final per JPA
    
    private String name;

    // Relazione Uno-a-Molti: un Team ha molti TeamMember. 
    // cascade = CascadeType.ALL significa che se salvi/elimini un Team, salvi/elimini anche i suoi membri.
    // orphanRemoval = true cancella dal DB i membri rimossi dalla lista.
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id") // Crea la chiave esterna nella tabella dei TeamMember
    private List<TeamMember> members = new ArrayList<>(); // Rimosso final per JPA

    private final int maxMembers = 5; // Questo può rimanere final perché è una costante logica

    // Costruttore vuoto obbligatorio per JPA
    public Team() {
    }

    public Team(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }

    public String getName() { return name; }

    // Cambiato leggermente per non rompere il tracciamento di Hibernate, 
    // ma mantenendo l'immutabilità verso l'esterno
    public List<TeamMember> getMembers() {
        return java.util.Collections.unmodifiableList(members);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team)) return false;
        Team team = (Team) o;
        return id != null && id.equals(team.id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }

    // --- TUTTA LA TUA LOGICA DI BUSINESS RIMANE IDENTICA ---

    public void addMember(TeamMember member) {
        if (isFull()) {
            throw new IllegalStateException("Team pieno");
        }

        if (hasUser(member.getUser().getId())) {
            throw new IllegalStateException("Utente già presente");
        }

        if (member.isLeader() && members.stream().anyMatch(TeamMember::isLeader)) {
            throw new IllegalStateException("Leader già presente");
        }

        members.add(member);
    }

    public boolean isFull() {
        return members.size() >= maxMembers;
    }

    public boolean hasMinMembers() {
        return members.size() >= 2;
    }

    public boolean hasUser(String userId) {
        return members.stream()
                .anyMatch(m -> m.getUser().getId().equals(userId));
    }

    public void removeMember(String userId) {
        members.removeIf(
                m -> m.getUser().getId().equals(userId)
        );
    }

    public boolean isLeader(String userId) {
        return getLeader()
                .getUser()
                .getId()
                .equals(userId);
    }

    public TeamMember getLeader() {
        return members.stream()
                .filter(TeamMember::isLeader)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Leader non trovato"));
    }

    public int getSize() {
        return members.size();
    }
}