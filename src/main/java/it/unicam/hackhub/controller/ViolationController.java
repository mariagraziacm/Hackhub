
package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.Violation;
import it.unicam.hackhub.service.ViolationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/violations")
public class ViolationController {

    private final ViolationService service;

    public ViolationController(ViolationService service) {
        this.service = service;
    }

    // GET /api/violations -> Ritorna la lista di tutte le segnalazioni
    @GetMapping
    public ResponseEntity<List<Violation>> list() {
        List<Violation> violations = service.listViolations();
        return ResponseEntity.ok(violations);
    }

    // PUT /api/violations/{violationId}/resolve?status=DISQUALIFY_TEAM -> Risolve la violazione
    @PutMapping("/{violationId}/resolve")
    public ResponseEntity<String> resolveViolation(
            @PathVariable String violationId, 
            @RequestParam Violation.ViolationStatus status) {
        try {
            service.resolveViolation(violationId, status);
            return ResponseEntity.ok("Violazione gestita correttamente");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("ERRORE: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("ERRORE GENERICO: " + e.getMessage());
        }
    }
}
