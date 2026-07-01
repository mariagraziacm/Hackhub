package it.unicam.hackhub.model;

import it.unicam.hackhub.model.InviteState;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.PartecipationRequestState;


public class PartecipationRequest{
    private String id;
    private ParticipationRequestState requestState;
    private User user;
    private Team team;

    public PartecipationRequest(String id, User user, Team team){
        this.id = id;
        this.user = user;
        this.team = team;
        this.requestState = PartecipationRequest.PENDING;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Team getTeam() {
        return team;
    }

    public ParticipationRequestState getRequestState() {
        return requestState;
    }

    public void accept() {
        if (requestState != ParticipationRequestState.PENDING)
            throw new IllegalStateException("Request già processata");

        requestState = ParticipationRequestState.ACCEPTED;
    }
    public void decline() {
        if (requestState != ParticipationRequestState.PENDING)
            throw new IllegalStateException("Request già processata");

        requestState = ParticipationRequestState.DECLINED;
    }
}
