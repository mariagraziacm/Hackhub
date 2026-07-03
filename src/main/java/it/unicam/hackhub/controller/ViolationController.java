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
        return violationService.listViolations();
    }

    

    public void chooseDisqualifyTeam(String violationId) {
        try {
            violationService.handleDisqualifyTeam(violationId);
            System.out.println("SYSTEM: Team squalificato dall'organizzatore.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }

    public void chooseDisqualifyMember(String violationId) {
        try {
            violationService.handleDisqualifyMember(violationId);
            System.out.println("SYSTEM: Membro squalificato dall'organizzatore.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }

    public void chooseNoAction(String violationId) {
        try {
            violationService.handleNoAction(violationId);
            System.out.println("SYSTEM: Violazione archiviata senza provvedimenti.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }
}