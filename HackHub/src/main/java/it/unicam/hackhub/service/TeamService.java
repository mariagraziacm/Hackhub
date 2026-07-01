package main.java.it.unicam.hackhub.service;

import it.unicam.hackhub.repository.TeamRepository;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.model.TeamMember;

public class TeamService{
    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository){
        this.teamRepository = teamRepository;
    }

    public Team createTeam(String id, String name, User creator) {
        if (id == null || id.isBlank() || name == null || name.isBlank()) {
            throw new IllegalArgumentException("Dati del team non validi (ID o Nome mancanti)");
        }
        if (creator == null) {
            throw new IllegalArgumentException("Il creatore del team deve essere un utente valido");
        }
        if (teamRepository.isUserInAnyTeam(creator.getId())) {
            throw new IllegalStateException("L'utente '" + creator.getName() + "' fa già parte di un altro team!");
        }
        if (teamRepository.existsByName(name)) {
            throw new IllegalStateException("Esiste già un team chiamato '" + name + "'!");
        }

        Team team = new Team(id, name);

        TeamMember leaderMember = new TeamMember("TM-" + id, id, null, creator);
        team.addMember(leaderMember);

        Leader teamLeader = new Leader(creator.getName(), leaderMember);
        team.setLeader(teamLeader);

        teamRepository.save(team);
        return team;
    }

    public Team getById(String id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Team con ID " + id + " non trovato"));
    }
}