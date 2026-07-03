package it.unicam.hackhub.service;

import it.unicam.hackhub.model.*;
import it.unicam.hackhub.repository.HackathonRepository;
import it.unicam.hackhub.repository.SubmissionRepository;
import it.unicam.hackhub.state.ConclusoState;
import it.unicam.hackhub.state.InCorsoState;
import it.unicam.hackhub.state.InIscrizioneState;
import it.unicam.hackhub.state.InValutazioneState;

import java.util.List;

public class HackathonService {
    private final HackathonRepository repo;
    private final TeamService teamService;
    private final StaffService staffService;
    private final SubmissionRepository submissionRepo;

    public HackathonService(HackathonRepository repo, TeamService teamService, StaffService staffService, SubmissionRepository submissionRepo) {
        this.repo = repo;
        this.teamService = teamService;
        this.staffService = staffService;
        this.submissionRepo = submissionRepo;
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

    public void proclamaVincitore(String hackathonId, String teamId, String organizerId) {
        Hackathon hackathon = getById(hackathonId);

        if (!hackathon.getOrganizer().getId().equals(organizerId)) {
            throw new IllegalStateException("Solo l'organizzatore può proclamare il vincitore");
        }

        if (!(hackathon.getState() instanceof InValutazioneState)) {
            throw new IllegalStateException("Hackathon non in fase di valutazione");
        }

        Team team = teamService.getById(teamId);

        hackathon.getState().proclamaVincitore(hackathon, team);

        repo.save(hackathon);

        System.out.println("🏆 Vincitore proclamato: " + team.getName());
    }

    public List<Submission> getResults(String hackathonId, String organizerId) {

        Hackathon hackathon = repo.findById(hackathonId)
                .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));

        // 1. controllo organizzatore (UC requisito)
        if (!hackathon.getOrganizer().getId().equals(organizerId)) {
            throw new IllegalStateException("Non sei l'organizzatore");
        }

        // 2. controllo stato (UC dice: concluso)
        if (!(hackathon.getState() instanceof InValutazioneState
                || hackathon.getState() instanceof ConclusoState)) {
            throw new IllegalStateException("Hackathon non in fase corretta");
        }

        // 3. recupero risultati
        return submissionRepo.findByHackathonId(hackathonId);
    }
    public List<Team> getIscrizioni(String hackathonId, String organizerId) {

        Hackathon hackathon = repo.findById(hackathonId)
                .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));

        // 1. controllo autorizzazione
        if (!hackathon.getOrganizer().getId().equals(organizerId)) {
            throw new IllegalStateException("Non autorizzato");
        }

        // 2. controllo stato (UC coerente)
        if (!(hackathon.getState() instanceof InIscrizioneState
                || hackathon.getState() instanceof InCorsoState)) {
            throw new IllegalStateException("Hackathon non in fase di iscrizione o corso");
        }

        // 3. recupero team
        List<Team> teams = hackathon.getIscritti();

        if (teams.isEmpty()) {
            throw new IllegalStateException("Nessun team iscritto");
        }

        return teams;
    }
    public List<Hackathon> getStoricoStaff(String staffId) {

        // 1. verifica staff valido
        StaffMember staff = staffService.getById(staffId);

        // 2. prendi hackathon associati allo staff
        List<Hackathon> all = repo.findAll();

        List<Hackathon> storico = all.stream()
                .filter(h -> h.getState() instanceof ConclusoState)
                .filter(h -> isStaffInHackathon(h, staff))
                .toList();

        if (storico.isEmpty()) {
            throw new IllegalStateException("Nessun hackathon concluso trovato");
        }

        return storico;
    }
    private boolean isStaffInHackathon(Hackathon h, StaffMember staff) {

        return h.getOrganizer() != null && h.getOrganizer().getId().equals(staff.getId())
                || h.getMentors().stream().anyMatch(m -> m.getId().equals(staff.getId()))
                || (h.getJudge() != null && h.getJudge().getId().equals(staff.getId()));
    }
}