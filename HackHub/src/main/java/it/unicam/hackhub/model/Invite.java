package it.unicam.hackhub.model;


import it.unicam.hackhub.model.InviteState;
import it.unicam.hackhub.model.PartecipationRequest;

public class Invite {
    private String id;
    private InviteState state;
    private PartecipationRequest partecipationRequest;



    public Invite(String id, InviteState state, PartecipationRequest partecipationRequest) {
        this.id = id;
        this.state = state;
        this.partecipationRequest = partecipationRequest;
    }

    public String getId() {
        return id;
    }
    public InviteState getState() {
        return state;
    }
    public PartecipationRequest getPartecipationRequest() {
        return partecipationRequest;
    }
}