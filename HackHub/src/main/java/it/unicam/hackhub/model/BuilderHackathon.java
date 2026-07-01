package it.unicam.hackhub.model;

import it.unicam.hackhub.state.HackathonState;
import it.unicam.hackhub.model.Hackathon;

public class BuilderHackathon {
    private String id;
    private String name;
    private String specifications;

    public BuilderHackathon setData(String id, String name, int partecipanti, String specifications){
        this.id = id;
        this.name = name;
        this.partecipanti = partecipanti;
        this.specifications = specifications;
        return this;
    }

    public Hackathon build(){
        Hackathon h = new Hackathon();
        h.setId(this.id);
        h.setName(this.name);
        h.setPartecipants(this.partecipanti);
        h.setSpecifications(this.specifications);
        return h;
    }
}