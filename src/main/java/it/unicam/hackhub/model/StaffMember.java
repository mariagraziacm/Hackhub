package it.unicam.hackhub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "staff_members")
@Inheritance(strategy = InheritanceType.JOINED) 
public abstract class StaffMember {

    @Id
    private String id; 

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; 

    private String hackathonId; 

    
    protected StaffMember() {
    }

    public StaffMember(String id, User user, String hackathonId) {
        this.id = id;
        this.user = user;
        this.hackathonId = hackathonId;
    }

    
    public String getId() { return id; }
    public User getUser() { return user; }
    public String getHackathonId() { return hackathonId; }

    
    public void setId(String id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setHackathonId(String hackathonId) { this.hackathonId = hackathonId; }
}