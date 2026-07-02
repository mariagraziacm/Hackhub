package it.unicam.hackhub.service;

import it.unicam.hackhub.model.Invite;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.repository.InviteRepository;
import it.unicam.hackhub.service.TeamService;
import it.unicam.hackhub.repository.UserRepository;
import it.unicam.hackhub.model.TeamMember;
import it.unicam.hackhub.model.Role;
import it.unicam.hackhub.model.InviteState;
import it.unicam.hackhub.model.ParticipationRequest;

import java.util.UUID;

public class InviteService {

    private final InviteRepository repo;
    private final TeamService teamService;
    private final UserRepository userRepo;

    public InviteService(
            InviteRepository repo,
            TeamService teamService,
            UserRepository userRepo) {

        this.repo = repo;
        this.teamService = teamService;
        this.userRepo = userRepo;
    }

    public Invite sendInvite(String teamId, String userId) {

        Team team = teamService.getById(teamId);
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User non trovato"));

        if (team.isFull()) {
            throw new IllegalStateException("Team pieno");
        }

        boolean alreadyInvited = repo.findAll().stream()
                .anyMatch(i ->
                        i.getUser().getId().equals(userId)
                                && i.getTeam().getId().equals(teamId)
                                && i.getState() == InviteState.PENDING
                );

        if (alreadyInvited) {
            throw new IllegalStateException("Invito già esistente");
        }

        Invite invite = new Invite(
                UUID.randomUUID().toString(),
                user,
                team
        );

        repo.save(invite);
        return invite;
    }

    public void acceptInvite(String inviteId) {

        Invite invite = repo.findById(inviteId)
                .orElseThrow(() -> new IllegalStateException("Invite non trovata"));

        Team team = invite.getTeam();
        User user = invite.getUser();

        if (team.isFull()) {
            throw new IllegalStateException("Team pieno");
        }

        if (teamService.isUserInAnyTeam(user.getId())) {
            throw new IllegalStateException("Utente già in team");
        }

        invite.accept();

        team.addMember(new TeamMember(
                UUID.randomUUID().toString(),
                user,
                Role.MEMBER
        ));
    }
}