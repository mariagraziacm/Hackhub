package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.Hackathon;
import java.util.ArrayList;
import java.util.List;

public class HackathonRepository {
    private final List<Hackathon> hackathons = new ArrayList<>();

    public void save(Hackathon hackathon) {
        hackathons.add(hackathon);
    }

    public List<Hackathon> findAll() {
        return hackathons;
    }
    public boolean existsByName(String name) {
        return hackathons.stream()
                .anyMatch(h -> h.getName().equalsIgnoreCase(name));
    }
}