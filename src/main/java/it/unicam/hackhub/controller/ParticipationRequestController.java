package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.ParticipationRequest;
import it.unicam.hackhub.service.ParticipationRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/participation-requests")
public class ParticipationRequestController {
    
    private final ParticipationRequestService requestService;

    public ParticipationRequestController(ParticipationRequestService requestService) {
        this.requestService = requestService;
    }

    // POST /api/participation-requests -> Invia una richiesta di partecipazione
    @PostMapping
    public ResponseEntity<String> sendRequest(@RequestBody RequestPayload payload) {
        try {
            ParticipationRequest req = requestService.sendRequest(payload.getIdTeam(), payload.getIdUser());
            return ResponseEntity.ok("SYSTEM: Richiesta di partecipazione (" + req.getId() + ") " +
                    "inviata con successo al Leader del team '" + req.getTeam().getName() + "'.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }

    // PUT /api/participation-requests/{requestId}/accept?leaderId=XYZ -> Accetta la richiesta
    @PutMapping("/{requestId}/accept")
    public ResponseEntity<String> acceptRequest(
            @PathVariable String requestId, 
            @RequestParam String leaderId) {
        try {
            requestService.acceptRequest(requestId, leaderId);
            return ResponseEntity.ok("SYSTEM: Richiesta di partecipazione approvata! Membro aggiunto al team.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }

    // PUT /api/participation-requests/{requestId}/decline?leaderId=XYZ -> Rifiuta la richiesta
    @PutMapping("/{requestId}/decline")
    public ResponseEntity<String> declineRequest(
            @PathVariable String requestId, 
            @RequestParam String leaderId) {
        try {
            requestService.declineRequest(requestId, leaderId);
            return ResponseEntity.ok("SYSTEM: Richiesta di partecipazione rifiutata dal leader.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }

    // DTO Helper per mappare la richiesta JSON iniziale
    public static class RequestPayload {
        private String idTeam;
        private String idUser;

        public String getIdTeam() { return idTeam; }
        public void setIdTeam(String idTeam) { this.idTeam = idTeam; }
        public String getIdUser() { return idUser; }
        public void setIdUser(String idUser) { this.idUser = idUser; }
    }
}