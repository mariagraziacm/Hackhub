package main.java.it.unicam.hackhub.service;

import it.unicam.hackhub.repository.UsersRepository;
import it.unicam.hackhub.repository.TeamRepository;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.TeamMember;
import it.unicam.hackhub.model.PartecipationRequest;
import it.unicam.hackhub.model.Role;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PartecipationRequestService{
    private final ParticipationRequestRepository repo;
    private final TeamService teamService;
    private final UserRepository userRepo;

    public ParticipationRequestService(
            ParticipationRequestRepository repo,
            TeamService teamService,
            UserRepository userRepo) {

        this.repo = repo;
        this.teamService = teamService;
        this.userRepo = userRepo;
    }

    public ParticipationRequest sendRequest(String teamId, String userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User non trovato"));

        Team team = teamService.getById(teamId);

        if (team.isFull()) {
            throw new IllegalStateException("Team pieno");
        }

        if (teamService.isUserInAnyTeam(userId)) {
            throw new IllegalStateException("Utente già in team");
        }

        ParticipationRequest req = new ParticipationRequest(
                UUID.randomUUID().toString(),
                user,
                team
        );

        repo.save(req);
        return req;
    }

    public void acceptRequest(String requestId) {

        ParticipationRequest req = repo.findById(requestId)
                .orElseThrow(() -> new IllegalStateException("Request non trovata"));

        req.accept();

        Team team = req.getTeam();

        team.addMember(new TeamMember(
                UUID.randomUUID().toString(),
                req.getUser(),
                Role.MEMBER
        ));
    }

    public void declineRequest(String requestId) {

        ParticipationRequest req = repo.findById(requestId)
                .orElseThrow(() -> new IllegalStateException("Request non trovata"));

        req.decline();
    }
}