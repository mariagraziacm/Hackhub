package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.Violation;
import it.unicam.hackhub.service.ViolationService;

import java.util.List;

public class ViolationController {
    private final ViolationService service;

    public ViolationController(ViolationService service) {
        this.service = service;
    }

    public void resolveViolation(String violationId, Violation.ViolationStatus status) {
        try {
            service.resolveViolation(violationId, status);
            System.out.println("SYSTEM: Violazione gestita correttamente");
        } catch (Exception e) {
            System.out.println("ERRORE: " + e.getMessage());
        }
    }

    public List<Violation> list() {
        return service.listViolations();
    }
}