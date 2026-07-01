package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsersRepository {
    private final List<User> users = new ArrayList<>();

    public void save(User user) {
        users.add(user);
    }

    public Optional<User> findById(String id) {
        return users.stream()
                .filter(u -> u.getid().equals(id))
                .findFirst();
    }
}