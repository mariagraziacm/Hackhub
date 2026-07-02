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

  

    // Costruttore c
    public SubmissionService(SubmissionRepository repo, HackathonRepository hackathonRepo, TeamRepository teamRepo) {
        this.repo = repo;
        this.hackathonRepo = hackathonRepo;
        this.teamRepo = teamRepo;
    }

    public SubmissionService(SubmissionRepository repo) {
        this.repo = repo;
    }
    public void setHackathonRepo(HackathonRepository hackathonRepo) {
        this.hackathonRepo = hackathonRepo;
    }

    public void setTeamRepo(TeamRepository teamRepo) {
        this.teamRepo = teamRepo;
    }

    public Submission sendSubmission(String hackathonId, String teamId, String title, String description) {
        if (title == null || title.isBlank() || description == null || description.isBlank()) {
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

        // controllo  stato con State Pattern
        if (hackathonRepo != null) {
            Hackathon hackathon = hackathonRepo.findById(hackathonId)
                    .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));
            
            Team team = (teamRepo != null) ? teamRepo.findById(teamId).orElse(null) : null;
            
            // Delega il controllo dello stato all'hackathon
            hackathon.inviaSottomissione(team, submission);
        }

        repo.save(submission);
        return submission;
    }

    public Submission editSubmission(String submissionId, String title, String description) {
        Submission submission = repo.findById(submissionId)
                .orElseThrow(() -> new IllegalStateException("Sottomissione non trovata"));

        if (title == null || title.isBlank() || description == null || description.isBlank()) {
            throw new IllegalStateException("Campi sottomissione non validi");
        }

        // controllo dello stato prima della modifica
        if (hackathonRepo != null) {
            Hackathon hackathon = hackathonRepo.findById(submission.getHackathonId())
                    .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));
            
            Team team = (teamRepo != null) ? teamRepo.findById(submission.getTeamId()).orElse(null) : null;
            
            // Verifica se lo stato attuale permette modifiche/invii
            hackathon.inviaSottomissione(team, submission);
        }

        submission.update(title, description);
        return submission;
    }

    public Submission getById(String submissionId) {
        return repo.findById(submissionId)
                .orElseThrow(() -> new IllegalStateException("Sottomissione non trovata"));
    }
}