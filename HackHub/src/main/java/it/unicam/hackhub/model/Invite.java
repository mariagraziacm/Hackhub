package it.unicam.hackhub.model;


import it.unicam.hackhub.model.InviteState;
import it.unicam.hackhub.model.PartecipationRequest;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.User;

public class Invite {
    private final String id;
    private final User user;
    private final Team team;
    private InviteState state;

    public Invite(String id, User user, Team team) {
        this.id = id;
        this.user = user;
        this.team = team;
        this.state = InviteState.PENDING;
    }

    public void accept() {
        if (state != InviteState.PENDING)
            throw new IllegalStateException();
        state = InviteState.ACCEPTED;
    }

    public void decline() {
        if (state != InviteState.PENDING)
            throw new IllegalStateException();
        state = InviteState.DECLINED;
    }
}