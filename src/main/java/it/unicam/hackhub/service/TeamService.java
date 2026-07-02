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
        
        // Trova il membro che richiede il cambio e verifica che sia il LEADER
        TeamMember oldLeader = team.getMembers().stream()
                .filter(m -> m.getUserId().equals(requesterUserId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Membro richiedente non trovato nel team."));
                
        if (oldLeader.getRole() != Role.LEADER) {
            throw new IllegalStateException("Solo l'attuale leader può cedere il comando del team!");
        }
        
        // Trova il membro designato come nuovo leader
        TeamMember newLeader = team.getMembers().stream()
                .filter(m -> m.getUserId().equals(newLeaderUserId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("L'utente designato non fa parte di questo team!"));

        // Modifica direttamente i ruoli dei membri esistenti usando i setter (o modificando il campo se accessibile)
        // Se non hai i setter per il Role in TeamMember, usiamo la riflessione o cambiamo i ruoli ricreando la lista.
        // Visto che nel tuo codice usi i costruttori, rimuoviamo passando l'oggetto TeamMember esatto trovato:
        
        team.getMembers().remove(oldLeader);
        team.getMembers().remove(newLeader);

        // Ri-aggiungiamo i membri con i ruoli invertiti
        team.addMember(new TeamMember(oldLeader.getId(), oldLeader.getUser(), Role.MEMBER));
        team.addMember(new TeamMember(newLeader.getId(), newLeader.getUser(), Role.LEADER));
        
        repo.save(team);
    }

    // UC: Rimozione Membro (effettuabile solo dal leader)
    public void removeMember(String leaderUserId, String teamId, String memberUserId) {
        Team team = getById(teamId);
        
        boolean isLeader = team.getMembers().stream()
                .anyMatch(m -> m.getUserId().equals(leaderUserId) && m.getRole() == Role.LEADER);

        if (!isLeader) {
            throw new IllegalStateException("Solo il leader può espellere componenti dal team.");
        }
        if (leaderUserId.equals(memberUserId)) {
            throw new IllegalStateException("Il leader non può espellere se stesso, deve prima cedere la leadership.");
        }
        
        // Trova l'oggetto TeamMember esatto per la rimozione
        TeamMember memberToRemove = team.getMembers().stream()
                .filter(m -> m.getUserId().equals(memberUserId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("L'utente non è presente in questo team."));
        
        team.getMembers().remove(memberToRemove);
        repo.save(team);
    }

    // UC: Abbandono volontario del Team da parte di un membro ordinario
    public void leaveTeam(String teamId, String userId) {
        Team team = getById(teamId);
        
        TeamMember member = team.getMembers().stream()
                .filter(m -> m.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Non fai parte di questo team."));
        
        if (member.getRole() == Role.LEADER) {
            throw new IllegalStateException("Un leader non può abbandonare il team direttamente. Promuovi prima qualcun altro!");
        }
        
        team.getMembers().remove(member);
        repo.save(team);
    }
}