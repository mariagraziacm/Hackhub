package main.java.it.unicam.hackhub.service;

import it.unicam.hackhub.model.Invite;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.User;

import java.util.UUID;

public class InviteService {
    private final InviteRepository repo;
    private final TeamService teamService;
    private final UserRepository userRepo;

    public Invite sendInvite(String teamId, String userId) {

        Team team = teamService.getById(teamId);
        User user = userRepo.findById(userId)
                .orElseThrow();

        if (team.isFull()) {
            throw new IllegalStateException();
        }

        Invite invite = new Invite(
                UUID.randomUUID().toString(),
                user,
                team
        );

        repo.save(invite);
        return invite;
    }
}
