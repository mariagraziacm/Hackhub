package main.java.it.unicam.hackhub.service;

import it.unicam.hackhub.repository.UsersRepository;
import it.unicam.hackhub.repository.TeamRepository;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.TeamMember;
import it.unicam.hackhub.model.PartecipationRequest;


import java.util.ArrayList;
import java.util.List;

public class PartecipationRequestService{
    private final TeamRepository teamRepository;
    private final UsersRepository usersRepository;
    private final List<PartecipationRequestService> requests = new ArrayList<>();

    public PartecipationRequestService(TeamRepository teamRepository, UsersRepository usersRepository){
        this.teamRepository = teamRepository;
        this.usersRepository = usersRepository;
    }

    public PartecipationRequest sendRequest(String teamId, String userId){
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Utente richiedente non trovato"));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalStateException("Team selezionato non esistente"));

        if (teamRepository.isUserInAnyTeam(userId)){
            throw new IllegalStateException("Impossibile inviare la richiesta: fai già parte di un team!");
        }
        if (team.isAlCompleto()){
            throw new IllegalStateException("Il team '" + team.getName() + "' è al completo!");
        }

        String requestId = "REQ-" + (requests.size() + 1);
        PartecipationRequest request = new PartecipationRequest(requestId, user, team);
        requests.add(request);
        return request;
    }

    public void acceptRequest(String requestId){
        PartecipationRequest req = requests.stream()
                .filter(r -> r.getId().equals(requestId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Richiesta non trovata"));

        req.accept();
        Team team = req.getTeam();

        TeamMember newMember = new TeamMember("TM-" + req.getId(), team.getId(), null, req.getUser());
        team.addMember(newMember);
    }

    public void declineRequest(String requestId){
        requests.stream()
                .filter(r -> r.getId().equals(requestId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Richiesta non trovata"))
                .decline();
    }

    public List<PartecipationRequest> getRequests(){
        return requests;
    }
}