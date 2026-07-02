package it.unicam.hackhub.repository;

import it.unicam.hackhub.model.Submission;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubmissionRepository {
    private final List<Submission> submissions = new ArrayList<>();

    public void save(Submission submission) {
        submissions.add(submission);
    }

    public List<Submission> findAll() {
        return submissions;
    }

    public Optional<Submission> findById(String id) {
        return submissions.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    public Optional<Submission> findByHackathonIdAndTeamId(String hackathonId, String teamId) {
        return submissions.stream()
                .filter(s -> s.getHackathonId().equals(hackathonId)
                        && s.getTeamId().equals(teamId))
                .findFirst();
    }

    public List<Submission> findByHackathonId(String hackathonId) {
        return submissions.stream()
                .filter(s -> s.getHackathonId().equals(hackathonId))
                .toList();
    }

    public boolean existsByHackathonIdAndTeamId(String hackathonId, String teamId) {
        return submissions.stream()
                .anyMatch(s -> s.getHackathonId().equals(hackathonId)
                        && s.getTeamId().equals(teamId));
    }

    public void deleteById(String id) {
        submissions.removeIf(s -> s.getId().equals(id));
    }
}
