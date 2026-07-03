package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.Submission;
import it.unicam.hackhub.service.SubmissionService;

import java.util.List;

public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    public Submission sendSubmission(String hackathonId,
                                     String teamId,
                                     String title,
                                     String description) {
        return submissionService.sendSubmission(hackathonId, teamId, title, description);
    }

    public Submission editSubmission(String submissionId,
                                     String title,
                                     String description) {
        return submissionService.editSubmission(submissionId, title, description);
    }

    public Submission getSubmissionById(String submissionId) {
        return submissionService.getById(submissionId);
    }

    public void valutaSottomissione(String hackathonId,
                                    String teamId,
                                    int score,
                                    String comment,
                                    String judgeId) {
        try {
            submissionService.valutaSottomissione(
                    hackathonId,
                    teamId,
                    score,
                    comment,
                    judgeId
            );
            System.out.println("SYSTEM: Valutazione salvata con successo");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
    public void viewEvaluation(String teamId, String hackathonId) {
        try {
            Submission s = submissionService.getEvaluation(teamId, hackathonId);

            System.out.println("=== VALUTAZIONE ===");
            System.out.println("Score: " + s.getScore());
            System.out.println("Commento: " + s.getComment());

        } catch (Exception e) {
            System.out.println("ERRORE: " + e.getMessage());
        }
    }
    public void viewSubmissions(String hackathonId, String staffId) {
        try {
            List<Submission> subs =
                    submissionService.getSubmissionsByHackathon(hackathonId, staffId);

            subs.forEach(s ->
                    System.out.println(s.getTeamId() + " - " + s.getTitle())
            );

        } catch (Exception e) {
            System.out.println("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }
}