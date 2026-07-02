package it.unicam.hackhub.repository;
import it.unicam.hackhub.model.ParticipationRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParticipationRequestRepository {
    private final List<ParticipationRequest> requests = new ArrayList<>();

    public void save(ParticipationRequest request) {
        requests.add(request);
    }

    public Optional<ParticipationRequest> findById(String id) {
        return requests.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }

    public List<ParticipationRequest> findAll() {
        return List.copyOf(requests);
    }
}