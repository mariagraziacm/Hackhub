package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.Violation;
import it.unicam.hackhub.service.ViolationService;

import java.util.List;

public class ViolationController {

    private final ViolationService violationService;

    public ViolationController(ViolationService violationService) {
        this.violationService = violationService;
    }

    public List<Violation> showPendingViolations() {
        return violationService.listPendingViolations();
    }

    public Violation showViolationDetails(String violationId) {
        return violationService.getById(violationId);
    }

    public void chooseDisqualifyTeam(String violationId) {
        violationService.disqualifyTeam(violationId);
    }

    public void chooseDisqualifyMember(String violationId, String memberId) {
        violationService.disqualifyMember(violationId, memberId);
    }

    public void chooseNoAction(String violationId) {
        violationService.noAction(violationId);
    }
}
