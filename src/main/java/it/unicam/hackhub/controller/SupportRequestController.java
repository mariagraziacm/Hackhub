package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.Call;
import it.unicam.hackhub.model.SupportRequest;
import it.unicam.hackhub.service.SupportRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support-requests")
public class SupportRequestController {
    
    private final SupportRequestService service;

    public SupportRequestController(SupportRequestService service) {
        this.service = service;
    }

    
    @PostMapping
    public ResponseEntity<String> sendSupportRequest(@RequestBody SupportRequestPayload payload) {
        try {
            service.sendRequest(
                    payload.getTeamId(),
                    payload.getMemberId(),
                    payload.getMentorId(),
                    payload.getHackathonId(),
                    payload.getMessage()
            );
            return ResponseEntity.ok("Richiesta di supporto inviata con successo");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERRORE: " + e.getMessage());
        }
    }

    
    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<?> showMentorRequests(@PathVariable String mentorId) {
        try {
            List<SupportRequest> requests = service.getMentorRequests(mentorId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }

   
    @PostMapping("/plan-call")
    public ResponseEntity<String> planCall(@RequestBody PlanCallPayload payload) {
        try {
            Call call = service.planCall(payload.getMentorId(), payload.getRequestId(), payload.getSlotId());
            return ResponseEntity.ok("SYSTEM: Call creata con ID " + call.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERRORE: " + e.getMessage());
        }
    }

    
    public static class SupportRequestPayload {
        private String teamId;
        private String memberId;
        private String mentorId;
        private String hackathonId;
        private String message;

        public String getTeamId() { return teamId; }
        public void setTeamId(String teamId) { this.teamId = teamId; }
        public String getMemberId() { return memberId; }
        public void setMemberId(String memberId) { this.memberId = memberId; }
        public String getMentorId() { return mentorId; }
        public void setMentorId(String mentorId) { this.mentorId = mentorId; }
        public String getHackathonId() { return hackathonId; }
        public void setHackathonId(String hackathonId) { this.hackathonId = hackathonId; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

   
    public static class PlanCallPayload {
        private String mentorId;
        private String requestId;
        private String slotId;

        public String getMentorId() { return mentorId; }
        public void setMentorId(String mentorId) { this.mentorId = mentorId; }
        public String getRequestId() { return requestId; }
        public void setRequestId(String requestId) { this.requestId = requestId; }
        public String getSlotId() { return slotId; }
        public void setSlotId(String slotId) { this.slotId = slotId; }
    }
}