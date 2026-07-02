package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.Invite;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class InviteRepository {
    private final List<Invite> invites = new ArrayList<>();

    public void save(Invite invite) {
        invites.add(invite);
    }

    public Optional<Invite> findById(String id) {
        return invites.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();
    }

    public List<Invite> findAll() {
        return List.copyOf(invites);
    }
}
