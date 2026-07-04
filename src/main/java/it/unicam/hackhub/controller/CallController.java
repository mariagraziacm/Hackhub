package it.unicam.hackhub.controller;

import it.unicam.hackhub.service.CallService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calls")
public class CallController {

    private final CallService callService;

    public CallController(CallService callService) {
        this.callService = callService;
    }

    // PUT /api/calls/{callId}/response -> Gestisce la risposta del team (accetta o rifiuta)
    @PutMapping("/{callId}/response")
    public ResponseEntity<String> handleCallResponse(
            @PathVariable String callId, 
            @RequestBody CallResponsePayload payload) {
        try {
            callService.respondToCall(callId, payload.isAccept());

            String status = payload.isAccept() ? "ACCETTATA" : "RIFIUTATA";
            return ResponseEntity.ok("Successo: La chiamata con ID " + callId + " è stata " + status + ".");

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Errore di business: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore imprevisto nella gestione della chiamata: " + e.getMessage());
        }
    }

    // DTO per gestire il corpo della risposta JSON
    public static class CallResponsePayload {
        private boolean accept;

        public boolean isAccept() { return accept; }
        public void setAccept(boolean accept) { this.accept = accept; }
    }
}