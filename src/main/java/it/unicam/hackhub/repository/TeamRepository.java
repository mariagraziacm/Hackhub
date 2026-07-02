package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.Team;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeamRepository {
    private final List<Team> teams = new ArrayList<>();

    public void save(Team team) {
    // Se esiste già, lo rimuove per non duplicarlo, poi inserisce quello aggiornato
    teams.removeIf(t -> t.getId().equals(team.getId()));
    teams.add(team);
}

    public List<Team> findAll() {
        return teams;
    }

    public Optional<Team> findById(String id) {
        return teams.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    public boolean existsByName(String name) {
        return teams.stream()
                .anyMatch(t -> t.getName().equalsIgnoreCase(name));
    }

    public boolean isUserInAnyTeam(String userId) {
        return teams.stream()
                .flatMap(team -> team.getMembers().stream())
                .anyMatch(member -> member.getUser().getId().equals(userId));
    }
}