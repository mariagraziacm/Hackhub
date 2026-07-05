package it.unicam.hackhub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "invites")
public class Invite {

    public enum InviteType {
        TEAM, MENTOR, JUDGE
    }

    public enum InviteState {
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
    @JoinColumn(name = "team_id")
    private Team team; 

    private String hackathonId; 
    @Enumerated(EnumType.STRING)
    private InviteType inviteType; 

    @Enumerated(EnumType.STRING)
    private InviteState state;

    
    public Invite() {
    }

    
    public Invite(String id, User user, Team team) {
        this.id = id;
        this.user = user;
        this.team = team;
        this.hackathonId = null;
        this.inviteType = InviteType.TEAM;
        this.state = InviteState.PENDING;
    }

    
    public Invite(String id, User user, String hackathonId, String typeStr) {
        if (hackathonId == null || hackathonId.isBlank()) {
            throw new IllegalStateException("hackathonId non valido");
        }
        
       
        InviteType t = InviteType.valueOf(typeStr.toUpperCase());
        
        if (t == null) {
            throw new IllegalStateException("inviteType non valido");
        }

        this.id = id;
        this.user = user;
        this.team = null;
        this.hackathonId = hackathonId;
        this.inviteType = t; 
        this.state = InviteState.PENDING;
    }

    

    public void accept() {
        if (state != InviteState.PENDING) {
            throw new IllegalStateException("Invito non più pending");
        }
        state = InviteState.ACCEPTED;
    }

    public void decline() {
        if (state != InviteState.PENDING) {
            throw new IllegalStateException("Invito non più pending");
        }
        state = InviteState.DECLINED;
    }

    
    public String getId() { return id; }
    public User getUser() { return user; }
    public Team getTeam() { return team; }
    public String getHackathonId() { return hackathonId; }
    public InviteType getInviteType() { return inviteType; }
    public InviteState getState() { return state; }

    
    public void setId(String id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setTeam(Team team) { this.team = team; }
    public void setHackathonId(String hackathonId) { this.hackathonId = hackathonId; }
    public void setInviteType(InviteType inviteType) { this.inviteType = inviteType; }
    public void setState(InviteState state) { this.state = state; }

    public boolean isTeamInvite() {
        return InviteType.TEAM.equals(inviteType);
    }

    public boolean isHackathonInvite() {
        return InviteType.MENTOR.equals(inviteType) || InviteType.JUDGE.equals(inviteType);
    }
}