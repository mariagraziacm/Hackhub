package main.java.it.unicam.hackhub.service;

import it.unicam.hackhub.repository.HackathonRepository;
import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.BuilderHackathon;
import  it.unicam.hackhub.model.Team;

public class HackathonService{
    private final HackathonRepository repo;
    private final TeamService teamService;

    public HackathonService(HackathonRepository repo, TeamService teamService) {
        this.repo = repo;
        this.teamService = teamService;
    }

    public Hackathon createHackathon(String id, String name, String specs) {

        if (id == null || id.isBlank() || name == null || name.isBlank()) {
            throw new IllegalArgumentException("Dati non validi");
        }

        if (repo.existsByName(name)) {
            throw new IllegalStateException("Hackathon già esistente");
        }

        Hackathon h = new Hackathon(id, name);
        h.setSpecifications(specs);

        repo.save(h);
        return h;
}