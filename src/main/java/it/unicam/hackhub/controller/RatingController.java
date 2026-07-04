package it.unicam.hackhub.controller;

import it.unicam.hackhub.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    // POST /api/ratings -> Invia la valutazione per una sottomissione
    @PostMapping
    public ResponseEntity<String> rate(@RequestBody RatePayload payload) {
        try {
            ratingService.rateSubmission(
                    payload.getJudgeId(), 
                    payload.getSubmissionId(), 
                    payload.getScore(), 
                    payload.getComment()
            );
            return ResponseEntity.ok("Valutazione salvata con successo");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERRORE: " + e.getMessage());
        }
    }

    // DTO Helper per incapsulare il corpo della richiesta JSON
    public static class RatePayload {
        private String judgeId;
        private String submissionId;
        private int score;
        private String comment;

        public String getJudgeId() { return judgeId; }
        public void setJudgeId(String judgeId) { this.judgeId = judgeId; }
        public String getSubmissionId() { return submissionId; }
        public void setSubmissionId(String submissionId) { this.submissionId = submissionId; }
        public int getScore() { return score; }
        public void setScore(int score) { this.score = score; }
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
    }
}