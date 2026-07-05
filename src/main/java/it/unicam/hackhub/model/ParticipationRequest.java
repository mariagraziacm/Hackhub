package it.unicam.hackhub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "participation_requests")
public class ParticipationRequest {

    
    public enum ParticipationRequestState {
        PENDING,
        ACCEPTED,
        DECLINED
    }

    @Id
    private String id; 

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; 

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team; 

    @Enumerated(EnumType.STRING) 
    private ParticipationRequestState state;

    
    public ParticipationRequest() {
    }

    public ParticipationRequest(String id, User user, Team team) {
        this.id = id;
        this.user = user;
        this.team = team;
        this.state = ParticipationRequestState.PENDING;
    }

    public String getId() { return id; }
    public User getUser() { return user; }
    public Team getTeam() { return team; }
    public ParticipationRequestState getState() { return state; }

  
    public void setId(String id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setTeam(Team team) { this.team = team; }
    public void setState(ParticipationRequestState state) { this.state = state; }


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