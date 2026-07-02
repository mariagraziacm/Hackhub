package it.unicam.hackhub.service;

import it.unicam.hackhub.repository.TeamRepository;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.model.TeamMember;
import it.unicam.hackhub.model.Role;

import java.util.UUID;

public class TeamService {
    private final TeamRepository repo;

    public TeamService(TeamRepository repo) {
        this.repo = repo;
    }

    public Team createTeam(String id, String name, User creator) {
        if (repo.existsByName(name)) {
            throw new IllegalStateException("Nome team già esistente");
        }

        if (repo.isUserInAnyTeam(creator.getId())) {
            throw new IllegalStateException("Utente già in un team");
        }

        Team team = new Team(id, name);

        TeamMember leader = new TeamMember(
                UUID.randomUUID().toString(),
                creator,
                Role.LEADER
        );

        team.addMember(leader);

        repo.save(team);
        return team;
    }
    public Team getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Team non trovato"));
    }
    public boolean isUserInAnyTeam(String userId) {
        return repo.isUserInAnyTeam(userId);
    }
}