package it.unicam.hackhub.service;

import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.model.TeamMember;
import it.unicam.hackhub.repository.TeamRepository;
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
        TeamMember leaderMember = new TeamMember("ID", creator, TeamMember.Role.LEADER);
        team.addMember(leaderMember);
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

    // UC: Cambio Leader 
    public void changeLeader(String requesterUserId, String teamId, String newLeaderUserId) {
        Team team = getById(teamId);
        
        // Trova leader attuale 
        TeamMember oldLeaderMember = team.getMembers().stream()
                .filter(m -> m.getUserId().equals(requesterUserId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Membro richiedente non trovato nel team."));
                
        if (oldLeaderMember.getRole() != TeamMember.Role.LEADER) {
            throw new IllegalStateException("Solo l'attuale leader può cedere il comando del team!");
        }
        
        //  nuovo leader 
        TeamMember newLeaderMember = team.getMembers().stream()
                .filter(m -> m.getUserId().equals(newLeaderUserId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("L'utente designato non fa parte di questo team!"));

        // RimuovI i membri
        team.removeMember(requesterUserId);
        team.removeMember(newLeaderUserId);

        // Ri-aggiungi i membri invertendo i ruoli 
        team.addMember(new TeamMember(UUID.randomUUID().toString(), oldLeaderMember.getUser(), TeamMember.Role.MEMBER));
        team.addMember(new TeamMember(UUID.randomUUID().toString(), newLeaderMember.getUser(), TeamMember.Role.LEADER));
        
        repo.save(team);
    }

    // UC: Rimozione Membro 
    public void removeMember(String leaderUserId, String teamId, String memberUserId) {
        Team team = getById(teamId);
        
        if (!team.isLeader(leaderUserId)) {
            throw new IllegalStateException("Solo il leader può espellere componenti dal team.");
        }
        if (leaderUserId.equals(memberUserId)) {
            throw new IllegalStateException("Il leader non può espellere se stesso, deve prima cedere la leadership.");
        }
        if (!team.hasUser(memberUserId)) {
            throw new IllegalStateException("L'utente non è presente in questo team.");
        }
        
        team.removeMember(memberUserId);
        repo.save(team);
    }

    // UC: Abbandono del Team da parte di un membro 
    public void leaveTeam(String teamId, String userId) {
        Team team = getById(teamId);
        
        if (!team.hasUser(userId)) {
            throw new IllegalStateException("Non fai parte di questo team.");
        }
        if (team.isLeader(userId)) {
            throw new IllegalStateException("Un leader non può abbandonare il team direttamente. Promuovi prima qualcun altro!");
        }
        
        team.removeMember(userId);
        repo.save(team);
    }
}