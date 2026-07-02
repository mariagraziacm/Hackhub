package it.unicam.hackhub.service;

import it.unicam.hackhub.model.User;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.TeamMember;
import it.unicam.hackhub.model.ParticipationRequest;
import it.unicam.hackhub.model.Role;
import it.unicam.hackhub.repository.ParticipationRequestRepository;
import it.unicam.hackhub.service.TeamService;
import it.unicam.hackhub.repository.UserRepository;
import it.unicam.hackhub.model.ParticipationRequestState;


import java.util.UUID;

public class ParticipationRequestService {
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
        boolean alreadyRequested = repo.findAll().stream()
                .anyMatch(r ->
                        r.getUser().getId().equals(userId)
                                && r.getTeam().getId().equals(teamId)
                                && r.getState() == ParticipationRequestState.PENDING
                );

        if (alreadyRequested) {
            throw new IllegalStateException("Request già esistente");
        }
        if (teamService.isUserInAnyTeam(userId)) {
            throw new IllegalStateException("Utente già in team");
        }
        if (team.hasUser(userId)) {
            throw new IllegalStateException("Utente già nel team");
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

        Team team = req.getTeam();

        if (team.isFull()) {
            throw new IllegalStateException("Team pieno");
        }

        if (teamService.isUserInAnyTeam(req.getUser().getId())) {
            throw new IllegalStateException("Utente già in team");
        }

        req.accept();

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