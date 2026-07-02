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

    // UC: Abbandono team
    public void leaveTeam(String teamId, String userId) {
        Team team = getById(teamId);

        if (!team.hasUser(userId)) {
            throw new IllegalStateException("Utente non presente nel team");
        }

        if (team.isLeader(userId)) {
            throw new IllegalStateException("Il leader non può abbandonare senza prima nominare un successore");
        }

        team.removeMember(userId);
    }

    // UC: Rimozione membro
    public void removeMember(String leaderId, String teamId, String memberId) {
        Team team = getById(teamId);

        if (!team.isLeader(leaderId)) {
            throw new IllegalStateException("Solo il leader può rimuovere membri");
        }

        if (!team.hasUser(memberId)) {
            throw new IllegalStateException("Membro non presente nel team");
        }

        if (leaderId.equals(memberId)) {
            throw new IllegalStateException("Il leader non può rimuovere se stesso");
        }

        team.removeMember(memberId);
    }

    // UC: Cambio leader
    public void changeLeader(String currentLeaderId, String teamId, String newLeaderId) {
        Team team = getById(teamId);

        if (!team.isLeader(currentLeaderId)) {
            throw new IllegalStateException("Solo il leader attuale può nominare il successore");
        }

        if (!team.hasUser(newLeaderId)) {
            throw new IllegalStateException("Il nuovo leader deve essere membro del team");
        }

        if (currentLeaderId.equals(newLeaderId)) {
            throw new IllegalStateException("Il nuovo leader coincide con quello attuale");
        }

        TeamMember currentLeader = team.getLeader();

        TeamMember nextLeader = team.getMembers().stream()
                .filter(m -> m.getUserId().equals(newLeaderId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Nuovo leader non trovato"));

        // role è final in TeamMember -> ricreo i due membri con ruoli invertiti
        team.removeMember(currentLeaderId);
        team.removeMember(newLeaderId);

        team.addMember(new TeamMember(
                currentLeader.getId(),
                currentLeader.getUser(),
                Role.MEMBER
        ));

        team.addMember(new TeamMember(
                nextLeader.getId(),
                nextLeader.getUser(),
                Role.LEADER
        ));
    }
}