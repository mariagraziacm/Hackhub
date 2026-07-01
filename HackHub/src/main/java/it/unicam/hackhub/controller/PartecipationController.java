package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.PartecipationRequest;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.repository.TeamRepository;
import it.unicam.hackhub.repository.UsersRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PartecipationController {
    private final TeamRepository teamRepository;
    private final UsersRepository usersRepository;
    private final List<PartecipationRequest> requests = new ArrayList<>();

    public PartecipationController(TeamRepository teamRepository, UsersRepository usersRepository) {
        this.teamRepository = teamRepository;
        this.usersRepository = usersRepository;
    }

    public void sendRequest(String idTeam, String idUser) {
        Optional<User> userOpt = usersRepository.findById(idUser);
        if (userOpt.isEmpty()) {
            System.out.println("Errore: Utente richiedente non trovato!");
            return;
        }
        User richiedente = userOpt.get();

        Optional<Team> teamOpt = teamRepository.findById(idTeam);
        if (teamOpt.isEmpty()) {
            System.out.println("Errore: Team selezionato non esistente!");
            return;
        }
        Team team = teamOpt.get();

        if(team.isAlCompleto()){
            System.out.println("SYSTEM [ERRORE]: Il team '" + team.getName() + "' è al completo!");
            return;
        }

        String requestId = "REQ-" + (requests.size() + 1);

        PartecipationRequest newRequest = new PartecipationRequest(requestId, richiedente, team);

        requests.add(newRequest);

        System.out.println("SYSTEM: Richiesta di partecipazione (" + requestId + ") inviata con successo al Leader del team '" + team.getName() + "'.");
    }
    public List<PartecipationRequest> getRequests() {
        return this.requests;
    }
}