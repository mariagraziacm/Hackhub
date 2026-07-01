package it.unicam.hackhub.model;

import it.unicam.hackhub.model.InviteState;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.PartecipationRequestState;


public class PartecipationRequest{
    private final String id;
    private final User user;
    private final Team team;
    private RequestState state;

    public ParticipationRequest(String id, User user, Team team) {
        this.id = id;
        this.user = user;
        this.team = team;
        this.state = PartecipationRequestState.PENDING;
    }

    public String getId() { return id; }
    public User getUser() { return user; }
    public Team getTeam() { return team; }
    public PartecipationRequestState getState() { return state; }

    public void accept() {
        if (state != PartecipationRequestState.PENDING)
            throw new IllegalStateException("Already processed");
        state = PartecipationRequestState.ACCEPTED;
    }

    public void decline() {
        if (state != PartecipationRequestState.PENDING)
            throw new IllegalStateException("Already processed");
        state = PartecipationRequestState.DECLINED;
    }
}