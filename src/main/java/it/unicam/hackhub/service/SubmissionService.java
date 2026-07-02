package it.unicam.hackhub.service;

import it.unicam.hackhub.model.Submission;
import it.unicam.hackhub.repository.SubmissionRepository;

import java.util.UUID;

public class SubmissionService {

    private final SubmissionRepository repo;

    public SubmissionService(SubmissionRepository repo) {
        this.repo = repo;
    }

    // UC: invia sottomissione
     
    public Submission sendSubmission(String hackathonId,
                                     String teamId,
                                     String title,
                                     String description) {

        if (hackathonId == null || hackathonId.isBlank()) {
            throw new IllegalStateException("Hackathon non valido");
        }

        if (teamId == null || teamId.isBlank()) {
            throw new IllegalStateException("Team non valido");
        }

        if (title == null || title.isBlank()
                || description == null || description.isBlank()) {
            throw new IllegalStateException("Campi sottomissione non validi");
        }

        if (repo.existsByHackathonIdAndTeamId(hackathonId, teamId)) {
            throw new IllegalStateException("Sottomissione già presente per questo team/hackathon");
        }

        Submission submission = new Submission(
                UUID.randomUUID().toString(),
                hackathonId,
                teamId,
                title,
                description
        );

        repo.save(submission);
        return submission;
    }

    /**
     * UC: modifica sottomissione
     */
    public Submission editSubmission(String submissionId,
                                     String title,
                                     String description) {

        Submission submission = repo.findById(submissionId)
                .orElseThrow(() -> new IllegalStateException("Sottomissione non trovata"));

        if (title == null || title.isBlank()
                || description == null || description.isBlank()) {
            throw new IllegalStateException("Campi sottomissione non validi");
        }

        submission.update(title, description);
        return submission;
    }

    public Submission getById(String submissionId) {
        return repo.findById(submissionId)
                .orElseThrow(() -> new IllegalStateException("Sottomissione non trovata"));
    }   
   } 