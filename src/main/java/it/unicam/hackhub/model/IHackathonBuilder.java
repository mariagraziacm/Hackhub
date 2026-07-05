package it.unicam.hackhub.model;

public interface IHackathonBuilder {
    IHackathonBuilder setId(String id);
    IHackathonBuilder setName(String name);
    IHackathonBuilder setSpecifications(String specifications);
    Hackathon build();
}//ciao
