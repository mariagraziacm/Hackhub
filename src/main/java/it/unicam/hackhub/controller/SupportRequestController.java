package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.Call;
import it.unicam.hackhub.model.SupportRequest;
import it.unicam.hackhub.service.SupportRequestService;

import java.util.List;

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
        try {
            List<SupportRequest> requests = service.getMentorRequests(mentorId);

            requests.forEach(r ->
                    System.out.println(
                            "TEAM: " + r.getTeamId() +
                                    " | MSG: " + r.getMessage() +
                                    " | STATUS: " + r.getStatus()
                    )
            );

        } catch (Exception e) {
            System.out.println("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }
    public void planCall(String mentorId, String requestId, String slotId) {
        try {
            Call call = service.planCall(mentorId, requestId, slotId);
            System.out.println("SYSTEM: Call creata con ID " + call.getId());
        } catch (Exception e) {
            System.out.println("ERRORE: " + e.getMessage());
        }
    }
    
}