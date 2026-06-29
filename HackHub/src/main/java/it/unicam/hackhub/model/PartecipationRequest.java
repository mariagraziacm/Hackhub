package it.unicam.hackhub.model;

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
