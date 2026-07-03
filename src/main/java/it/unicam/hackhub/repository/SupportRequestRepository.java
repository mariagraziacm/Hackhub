package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.SupportRequest;

import java.util.ArrayList;
import java.util.List;

public class SupportRequestRepository {
    private final List<SupportRequest> requests = new ArrayList<>();

    public void save(SupportRequest request) {
        requests.removeIf(r -> r.getId().equals(request.getId()));
        requests.add(request);
    }

    public List<SupportRequest> findAll() {
        return new ArrayList<>(requests);
    }

    public List<SupportRequest> findByMentorId(String mentorId) {
        return requests.stream()
                .filter(r -> r.getMentorId().equals(mentorId))
                .toList();
    }
}
