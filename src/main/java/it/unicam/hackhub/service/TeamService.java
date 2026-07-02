package it.unicam.hackhub.service;

import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.model.TeamMember;
import it.unicam.hackhub.model.Role;
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
        TeamMember leader = new TeamMember(UUID.randomUUID().toString(), creator, Role.LEADER);
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

    // UC: Cambio Leader (effettuabile solo dal leader attuale)
    public void changeLeader(String requesterUserId, String teamId, String newLeaderUserId) {
        Team team = getById(teamId);
        
        // 1. Trova l'oggetto TeamMember del leader attuale per estrarre l'utente User
        TeamMember oldLeaderMember = team.getMembers().stream()
                .filter(m -> m.getUserId().equals(requesterUserId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Membro richiedente non trovato nel team."));
                
        if (oldLeaderMember.getRole() != Role.LEADER) {
            throw new IllegalStateException("Solo l'attuale leader può cedere il comando del team!");
        }
        
        // 2. Trova l'oggetto TeamMember del nuovo leader per estrarre l'utente User
        TeamMember newLeaderMember = team.getMembers().stream()
                .filter(m -> m.getUserId().equals(newLeaderUserId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("L'utente designato non fa parte di questo team!"));

        // 3. Usa i metodi interni di Team per rimuovere i nodi in modo sicuro
        team.removeMember(requesterUserId);
        team.removeMember(newLeaderUserId);

        // 4. Ri-aggiungi i membri invertendo i ruoli usando il metodo addMember nativo di Team
        team.addMember(new TeamMember(UUID.randomUUID().toString(), oldLeaderMember.getUser(), Role.MEMBER));
        team.addMember(new TeamMember(UUID.randomUUID().toString(), newLeaderMember.getUser(), Role.LEADER));
        
        repo.save(team);
    }

    // UC: Rimozione Membro (effettuabile solo dal leader)
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

    // UC: Abbandono volontario del Team da parte di un membro ordinario
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