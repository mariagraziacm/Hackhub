package it.unicam.hackhub.service;

import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.Judge;
import it.unicam.hackhub.model.Submission;
import it.unicam.hackhub.repository.HackathonRepository;
import it.unicam.hackhub.repository.SubmissionRepository;
import it.unicam.hackhub.state.InValutazioneState;

public class EvaluationService {
    private final SubmissionRepository submissionRepo;
    private final StaffService staffService;
    private final HackathonRepository hackathonRepo;

    public EvaluationService(SubmissionRepository submissionRepo,
                             StaffService staffService,
                             HackathonRepository hackathonRepo) {
        this.submissionRepo = submissionRepo;
        this.staffService = staffService;
        this.hackathonRepo = hackathonRepo;
    }

    public void evaluateSubmission(String judgeId,
                                   String submissionId,
                                   int score,
                                   String comment) {

        Judge judge = staffService.getJudge(judgeId);

        Submission submission = submissionRepo.findById(submissionId)
                .orElseThrow(() -> new IllegalStateException("Submission non trovata"));

        Hackathon hackathon = hackathonRepo.findById(submission.getHackathonId())
                .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));

        if (!(hackathon.getState() instanceof InValutazioneState)) {
            throw new IllegalStateException("Hackathon non in valutazione");
        }

        submission.evaluate(score, comment);

        submissionRepo.save(submission);
    }
}