package it.unicam.hackhub.controller;

import it.unicam.hackhub.service.CallService;

public class CallController {
    private final CallService callService;

    public CallController(CallService callService) {
        this.callService = callService;
    }

    public void handleCallResponse(String callId, boolean accept) {
        try {
            callService.respondToCall(callId, accept);
            
            String status = accept ? "ACCETTATA" : "RIFIUTATA";
            System.out.println("Successo: La chiamata con ID " + callId + " è stata " + status + ".");
            
        } catch (IllegalStateException e) {
            System.err.println("Errore di business: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Errore imprevisto nella gestione della chiamata: " + e.getMessage());
        }
    }
}
