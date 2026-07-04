package it.unicam.hackhub.controller;

import it.unicam.hackhub.service.RatingService;

public class RatingController {

    private final RatingService RatingService;

    public RatingController (RatingService RatingService) {
        this.RatingService = RatingService;
    }

    public void rate(String judgeId,
                         String submissionId,
                         int score,
                         String comment) {
        try {
            RatingService.rateSubmission(judgeId, submissionId, score, comment);
            System.out.println("Valutazione salvata con successo");
        } catch (Exception e) {
            System.out.println("ERRORE: " + e.getMessage());
        }
    }
}