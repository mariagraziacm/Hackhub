package it.unicam.hackhub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "team_members")
public class TeamMember {

    @Id
    private String id; 

    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false) 
    private User user; 
    @Enumerated(EnumType.STRING) 
    private Role role; 

    
    public enum Role {
        LEADER,
        MEMBER
    }

    
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