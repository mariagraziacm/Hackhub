package it.unicam.hackhub.controller;

import it.unicam.hackhub.service.ParticipationRequestService;
import it.unicam.hackhub.model.ParticipationRequest;

    public class ParticipationRequestController {
        private final ParticipationRequestService requestService;

        public ParticipationRequestController(ParticipationRequestService requestService) {
            this.requestService = requestService;
        }

        public void sendRequest(String idTeam, String idUser) {
            try {
                ParticipationRequest req = requestService.sendRequest(idTeam, idUser);
                System.out.println("SYSTEM: Richiesta di partecipazione (" + req.getId() + ") " +
                        "inviata con successo al Leader del team '" + req.getTeam().getName() + "'.");
            } catch (IllegalStateException e) {
                System.out.println("SYSTEM [ERRORE]: " + e.getMessage());
            }
        }


        public void acceptRequest(String requestId, String leaderId) {
            try {
                requestService.acceptRequest(requestId, leaderId);
                System.out.println("SYSTEM: Richiesta di partecipazione approvata! Membro aggiunto al team.");
            } catch (IllegalStateException e) {
                System.out.println("SYSTEM [ERRORE]: " + e.getMessage());
            }
        }

        public void declineRequest(String requestId, String leaderId) {
            try {
                requestService.declineRequest(requestId, leaderId);
                System.out.println("SYSTEM: Richiesta di partecipazione rifiutata dal leader.");
            } catch (IllegalStateException e) {
                System.out.println("SYSTEM [ERRORE]: " + e.getMessage());
            }
        }
    }