package it.unicam.hackhub.service;

import it.unicam.hackhub.repository.HackathonRepository;
import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.BuilderHackathon;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.Organizer;

import java.util.List;

public class HackathonService {
    private final HackathonRepository repo;
    private final TeamService teamService;
    private final StaffService staffService;

    public HackathonService(HackathonRepository repo, TeamService teamService, StaffService staffService) {
        this.repo = repo;
        this.teamService = teamService;
        this.staffService = staffService;
    }

    public Hackathon createHackathon(String id, String name, String specifications, String organizerId) {
        if (id == null || id.isBlank() || name == null || name.isBlank()) {
            throw new IllegalArgumentException("Campi non compilati o formato non valido");
        }

        if (repo.existsByName(name)) {
            throw new IllegalStateException("Hackathon già esistente");
        }

        Organizer organizer = staffService.getOrganizer(organizerId);
        
        Hackathon hackathon = new BuilderHackathon()
                .setId(id)
                .setName(name)
                .setSpecifications(specifications)
                .build();

        hackathon.setOrganizer(organizer);
        hackathon.setState(new it.unicam.hackhub.state.InIscrizioneState()); // Forza stato iniziale

        repo.save(hackathon);
        return hackathon;
    }

    public void addTeamToHackathon(String hackathonId, String teamId) {
        Hackathon hackathon = repo.findById(hackathonId)
                .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));

        Team team = teamService.getById(teamId);

        // Controllo requisiti almeno un partecipante/leader
        if (team.getMembers().isEmpty() && team.getLeader() == null) {
            throw new IllegalStateException("Il team non ha il numero giusto di partecipanti");
        }

        hackathon.iscriviTeam(team);
        repo.save(hackathon);
    }

    public void removeTeamFromHackathon(String hackathonId, String teamId) {
        Hackathon hackathon = repo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));

        Team team = teamService.getById(teamId);
        hackathon.disiscriviTeam(team);
        
        // Rimuove tutti i partecipanti del team dall'hackathon svuotando la lista locale all'occorrenza
        repo.save(hackathon);
    }

    public List<Hackathon> getAllHackathons() {
        return repo.findAll();
    }

    public Hackathon getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));
    }
    public void addMentorToHackathon(String hackathonId, String mentorId, String organizerId) {
    Hackathon hackathon = getById(hackathonId);
    
    if (!hackathon.getOrganizer().getId().equals(organizerId)) {
        throw new IllegalStateException("Solo l'organizzatore di questo hackathon può aggiungere mentori");
    }

    it.unicam.hackhub.model.Mentor mentor = staffService.getMentor(mentorId);
    hackathon.addMentor(mentor);
    repo.save(hackathon);
    }
}