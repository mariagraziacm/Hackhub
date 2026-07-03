package it.unicam.hackhub.controller;

import it.unicam.hackhub.service.SupportRequestService;

public class SupportRequestController {
    private final SupportRequestService service;

    public SupportRequestController(SupportRequestService service) {
        this.service = service;
    }

    public void sendSupportRequest(String teamId,
                                   String memberId,
                                   String mentorId,
                                   String hackathonId,
                                   String message) {

        try {
            service.sendRequest(teamId, memberId, mentorId, hackathonId, message);
            System.out.println("Richiesta di supporto inviata con successo");
        } catch (Exception e) {
            System.out.println("ERRORE: " + e.getMessage());
        }
    }

    public void showMentorRequests(String mentorId) {
        service.getMentorRequests(mentorId)
                .forEach(r -> System.out.println(
                        r.getTeamId() + " - " + r.getMessage()
                ));
    }
}