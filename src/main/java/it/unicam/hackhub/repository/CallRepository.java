package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.Call;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CallRepository {
    private final List<Call> calls = new ArrayList<>();

    public void save(Call call) {
        calls.removeIf(c -> c.getId().equals(call.getId()));
        calls.add(call);
    }

    public Optional<Call> findById(String id) {
        return calls.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    public List<Call> findAll() {
        return new ArrayList<>(calls);
    }
}
