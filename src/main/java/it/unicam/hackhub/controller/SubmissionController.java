package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.Submission;
import it.unicam.hackhub.service.SubmissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    // POST /api/submissions -> Invia una nuova sottomissione
    @PostMapping
    public ResponseEntity<?> sendSubmission(@RequestBody SubmissionPayload payload) {
        try {
            Submission submission = submissionService.sendSubmission(
                    payload.getHackathonId(),
                    payload.getTeamId(),
                    payload.getTitle(),
                    payload.getDescription()
            );
            return ResponseEntity.ok(submission);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }
    }

    // PUT /api/submissions/{submissionId} -> Modifica una sottomissione esistente
    @PutMapping("/{submissionId}")
    public ResponseEntity<?> editSubmission(
            @PathVariable String submissionId,
            @RequestBody SubmissionPayload payload) {
        try {
            Submission submission = submissionService.editSubmission(
                    submissionId,
                    payload.getTitle(),
                    payload.getDescription()
            );
            return ResponseEntity.ok(submission);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }
    }

    // GET /api/submissions/{submissionId} -> Recupera una sottomissione dal suo ID
    @GetMapping("/{submissionId}")
    public ResponseEntity<?> getSubmissionById(@PathVariable String submissionId) {
        try {
            Submission submission = submissionService.getById(submissionId);
            return ResponseEntity.ok(submission);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/submissions/evaluate -> Inserisce la valutazione di un giudice
    @PostMapping("/evaluate")
    public ResponseEntity<String> valutaSottomissione(@RequestBody EvaluationPayload payload) {
        try {
            submissionService.valutaSottomissione(
                    payload.getHackathonId(),
                    payload.getTeamId(),
                    payload.getScore(),
                    payload.getComment(),
                    payload.getJudgeId()
            );
            return ResponseEntity.ok("SYSTEM: Valutazione salvata con successo");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }
    }

    // GET /api/submissions/evaluation?teamId=XYZ&hackathonId=ABC -> Visualizza il voto ricevuto
    @GetMapping("/evaluation")
    public ResponseEntity<?> viewEvaluation(
            @RequestParam String teamId,
            @RequestParam String hackathonId) {
        try {
            Submission s = submissionService.getEvaluation(teamId, hackathonId);
            return ResponseEntity.ok(s);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERRORE: " + e.getMessage());
        }
    }

    // GET /api/submissions/hackathon/{hackathonId}?staffId=XYZ -> Elenca le sottomissioni per lo staff
    @GetMapping("/hackathon/{hackathonId}")
    public ResponseEntity<?> viewSubmissions(
            @PathVariable String hackathonId,
            @RequestParam String staffId) {
        try {
            List<Submission> subs = submissionService.getSubmissionsByHackathon(hackathonId, staffId);
            return ResponseEntity.ok(subs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }

    // DTO per gestire la creazione e modifica delle sottomissioni
    public static class SubmissionPayload {
        private String hackathonId;
        private String teamId;
        private String title;
        private String description;

        public String getHackathonId() { return hackathonId; }
        public void setHackathonId(String hackathonId) { this.hackathonId = hackathonId; }
        public String getTeamId() { return teamId; }
        public void setTeamId(String teamId) { this.teamId = teamId; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    // DTO per gestire l'input della valutazione
    public static class EvaluationPayload {
        private String hackathonId;
        private String teamId;
        private int score;
        private String comment;
        private String judgeId;

        public String getHackathonId() { return hackathonId; }
        public void setHackathonId(String hackathonId) { this.hackathonId = hackathonId; }
        public String getTeamId() { return teamId; }
        public void setTeamId(String teamId) { this.teamId = teamId; }
        public int getScore() { return score; }
        public void setScore(int score) { this.score = score; }
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
        public String getJudgeId() { return judgeId; }
        public void setJudgeId(String judgeId) { this.judgeId = judgeId; }
    }
}