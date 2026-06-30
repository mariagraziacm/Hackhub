package it.unicam.hackhub.model;

import it.unicam.hackhub.model.InviteState;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.model.Team;

public class PartecipationRequest {
    private String id;
    private InviteState requestState;
    private User user;
    private Team team;

    public PartecipationRequest(String id, User user, Team team) {
        this.id = id;
        this.user = user;
        this.team = team;
        this.requestState = InviteState.PENDING;
    }
}
