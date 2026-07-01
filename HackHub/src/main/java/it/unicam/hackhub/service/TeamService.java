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

    public Team createTeam(String id, String name, User creator){
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Nome non valido");
        }

        if (teamRepository.isUserInAnyTeam(creator.getid())) {
            throw new IllegalStateException("L'utente fa già parte di un altro team");
        }


        if (teamRepository.existsByName(name)){
            throw new IllegalStateException("Team già esistente");
        }

        Team team = new Team(id, name);

        TeamMember leader = new TeamMember("TM-" + id, id, null, creator);
        team.getMembers().add(leader);

        Leader leader = new Leader(creator.getName(), leaderMember);
        team.setLeader(leader);

        teamRepository.save(team);
        return team;
    }

    public Team getById(String id){
        return teamRepository.findById(id).orElseThrow(() -> new IllegalStateException("Team non trovato"));
    }
}
