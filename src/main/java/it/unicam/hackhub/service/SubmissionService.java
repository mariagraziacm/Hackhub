package it.unicam.hackhub.service;

import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.Submission;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.repository.SubmissionRepository;
import it.unicam.hackhub.repository.HackathonRepository;
import it.unicam.hackhub.repository.TeamRepository;
import java.util.UUID;

public class SubmissionService {
    private final SubmissionRepository repo;
    private HackathonRepository hackathonRepo;
    private TeamRepository teamRepo;

    public SubmissionService(SubmissionRepository repo, HackathonRepository hackathonRepo, TeamRepository teamRepo) {
        this.repo = repo;
        this.hackathonRepo = hackathonRepo;
        this.teamRepo = teamRepo;
    }

    public Submission sendSubmission(String hackathonId, String teamId, String title, String description) {
        if (title == null || title.isBlank() || description == null || description.isBlank()) {
            throw new IllegalStateException("Campi sottomissione non validi o nulli");
        }

        if (repo.existsByHackathonIdAndTeamId(hackathonId, teamId)) {
            throw new IllegalStateException("Sottomissione già presente");
        }

        Submission submission = new Submission(
                UUID.randomUUID().toString(),
                hackathonId,
                teamId,
                title,
                description
        );

        if (hackathonRepo != null) {
            Hackathon hackathon = hackathonRepo.findById(hackathonId)
                    .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));
            Team team = (teamRepo != null) ? teamRepo.findById(teamId).orElse(null) : null;
            hackathon.inviaSottomissione(team, submission);
        }

        repo.save(submission);
        return submission;
    }

    public Submission editSubmission(String submissionId, String title, String description) {
        if (title == null || title.isBlank() || description == null || description.isBlank()) {
            throw new IllegalStateException("I nuovi dati inseriti non sono corretti");
        }

        Submission submission = repo.findById(submissionId)
                .orElseThrow(() -> new IllegalStateException("Sottomissione non trovata"));

        if (hackathonRepo != null) {
            Hackathon hackathon = hackathonRepo.findById(submission.getHackathonId())
                    .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));
            Team team = (teamRepo != null) ? teamRepo.findById(submission.getTeamId()).orElse(null) : null;
            hackathon.inviaSottomissione(team, submission);
        }

        submission.update(title, description);
        repo.save(submission);
        return submission;
    }

    public Submission getById(String submissionId) {
        return repo.findById(submissionId)
                .orElseThrow(() -> new IllegalStateException("Sottomissione non trovata"));
    }
}