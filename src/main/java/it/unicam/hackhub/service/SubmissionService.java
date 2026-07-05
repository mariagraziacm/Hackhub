package it.unicam.hackhub.service;

import it.unicam.hackhub.model.*;
import it.unicam.hackhub.repository.SubmissionRepository;
import it.unicam.hackhub.repository.HackathonRepository;
import it.unicam.hackhub.repository.TeamRepository;
import it.unicam.hackhub.state.InValutazioneState;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class SubmissionService {
    private final SubmissionRepository repo;
    private final HackathonRepository hackathonRepo;
    private final TeamRepository teamRepo;
    private final StaffService staffService;

    public SubmissionService(SubmissionRepository repo, 
                             HackathonRepository hackathonRepo, 
                             TeamRepository teamRepo, 
                             StaffService staffService) {
        this.repo = repo;
        this.hackathonRepo = hackathonRepo;
        this.teamRepo = teamRepo;
        this.staffService = staffService;
    }

    @Transactional
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

        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));
        Team team = teamRepo.findById(teamId).orElse(null);
        
        hackathon.sendSubmission(team, submission);

        repo.save(submission);
        return submission;
    }

    @Transactional
    public Submission editSubmission(String submissionId, String title, String description) {
        if (title == null || title.isBlank() || description == null || description.isBlank()) {
            throw new IllegalStateException("I nuovi dati inseriti non sono corretti");
        }

        Submission submission = repo.findById(submissionId)
                .orElseThrow(() -> new IllegalStateException("Sottomissione non trovata"));

        Hackathon hackathon = hackathonRepo.findById(submission.getHackathonId())
                .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));
        Team team = teamRepo.findById(submission.getTeamId()).orElse(null);
        
        hackathon.sendSubmission(team, submission);

        submission.update(title, description);
        repo.save(submission);
        return submission;
    }

    @Transactional(readOnly = true)
    public Submission getById(String submissionId) {
        return repo.findById(submissionId)
                .orElseThrow(() -> new IllegalStateException("Sottomissione non trovata"));
    }

    @Transactional
    public void rateSubmission(
            String hackathonId,
            String teamId,
            int score,
            String comment,
            String judgeId
    ) {

        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));

        if (!(hackathon.getState() instanceof InValutazioneState)) {
            throw new IllegalStateException("Hackathon non in fase di valutazione");
        }


        staffService.getJudge(judgeId);

        Submission submission = repo.findByHackathonIdAndTeamId(hackathonId, teamId)
                .orElseThrow(() -> new IllegalStateException("Submission non trovata"));

        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new IllegalStateException("Team non trovato"));

        hackathon.getState().rateSubmission(
                hackathon,
                team,
                submission,
                score,
                comment
        );

        repo.save(submission);
    }

    @Transactional(readOnly = true)
    public Submission getEvaluation(String teamId, String hackathonId) {
        Submission submission = repo.findByHackathonIdAndTeamId(hackathonId, teamId)
                .orElseThrow(() -> new IllegalStateException("Nessuna submission trovata"));

        submission.ensureEvaluated();
        return submission;
    }

    @Transactional(readOnly = true)
    public List<Submission> getSubmissionsByHackathon(String hackathonId, String staffId) {
        StaffMember staff = staffService.getById(staffId);

    
        hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));

     
        if (!staff.getHackathonId().equals(hackathonId)) {
            throw new IllegalStateException("Staff non assegnato a questo hackathon");
        }

        List<Submission> submissions = repo.findByHackathonId(hackathonId);

        if (submissions.isEmpty()) {
            throw new IllegalStateException("Nessuna sottomissione trovata");
        }

        return submissions;
    }
}