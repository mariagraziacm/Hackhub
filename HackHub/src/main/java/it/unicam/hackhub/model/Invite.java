package it.unicam.hackhub.model;


import it.unicam.hackhub.model.InviteState;
import it.unicam.hackhub.model.PartecipationRequest;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.User;

public class Invite {
    private String id;
    private InviteState state;
    private User invitedUser;
    private Team team;

    public Invite(String id, InviteState state, User invitedUser, Team team) {
        this.id = id;
        this.state = state;
        this.invitedUser = invitedUser;
        this.team = team;
    }

    public String getId() {
        return id;
    };
    public InviteState getState() {
        return state;
    };
    public User getInvitedUser(){ return invitedUser;}
    public Team getTeam(){return team;}

    public void accept() {
        if (state != InviteState.PENDING)
            throw new IllegalStateException("Invite già processato");
        state = InviteState.ACCEPTED;
    }

    public void decline() {
        if (state != InviteState.PENDING)
            throw new IllegalStateException("Invite già processato");
        state = InviteState.DECLINED;
    }

}