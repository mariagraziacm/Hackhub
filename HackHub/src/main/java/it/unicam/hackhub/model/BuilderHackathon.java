package it.unicam.hackhub.model;

import it.unicam.hackhub.model.state.HackathonState;
import it.unicam.hackhub.state.InIscrizioneState;
import it.unicam.hackhub.model.Hackathon;

public class BuilderHackathon {
    private String id;
    private String name;
    private int partecipanti;
    private String specifications;
    private HackathonState hackathonState;

    public BuilderHackathon setData(String id, String name, int partecipanti, String specifications) {
        this.id = id;
        this.name = name;
        this.partecipanti = partecipanti;
        this.specifications = specifications;
        return this;
    }

    public BuilderHackathon setHackathonState() {
        this.hackathonState = new InIscrizioneState();
        return this;
    }

    public Hackathon build() {
        Hackathon h = new Hackathon();
        h.setId(this.id);
        h.setName(this.name);
        h.setPartecipanti(this.partecipanti);
        h.setSpecifications(this.specifications);
        h.setHackathonState(this.hackathonState);
        return h;
    }
}