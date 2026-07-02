package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.Submission;
import it.unicam.hackhub.service.SubmissionService;

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
}
