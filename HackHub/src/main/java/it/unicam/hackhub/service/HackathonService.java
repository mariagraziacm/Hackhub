package main.java.it.unicam.hackhub.service;

import it.unicam.hackhub.repository.HackathonRepository;
import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.BuilderHackathon;
import  it.unicam.hackhub.model.Team;

import java.util.List;

public class HackathonService{
    private final HackathonRepository repo;
    private final TeamService teamService;

    public HackathonService(HackathonRepository repo, TeamService teamService) {
        this.repo = repo;
        this.teamService = teamService;
    }

    public Hackathon createHackathon(String id, String name, String specifications) {
        if (id == null || id.isBlank() ||
                name == null || name.isBlank()) {
            throw new IllegalArgumentException("Dati non validi");
        }

        if (repo.existsByName(name)) {
            throw new IllegalStateException("Hackathon già esistente");
        }

        Hackathon hackathon = new BuilderHackathon()
                .setId(id)
                .setName(name)
                .setSpecifications(specifications)
                .build();

        repo.save(hackathon);
        return hackathon;
    }

    // gestione team
    public void addTeamToHackathon(String hackathonId, Team team) {

        Hackathon hackathon = repo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));

        hackathon.iscriviTeam(team);
    }

    public void removeTeamFromHackathon(String hackathonId, Team team) {

        Hackathon hackathon = repo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));

        hackathon.disiscriviTeam(team);
    }

    public List<Hackathon> getAllHackathons() {
        return repo.findAll();
    }

    public Hackathon getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));
    }
}