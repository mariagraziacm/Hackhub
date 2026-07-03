package it.unicam.hackhub.controller;

import it.unicam.hackhub.service.EvaluationService;

public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController (EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    public void evaluate(String judgeId,
                         String submissionId,
                         int score,
                         String comment) {
        try {
            evaluationService.evaluateSubmission(judgeId, submissionId, score, comment);
            System.out.println("Valutazione salvata con successo");
        } catch (Exception e) {
            System.out.println("ERRORE: " + e.getMessage());
        }
    }
}