package it.unicam.hackhub.service;

import it.unicam.hackhub.model.Violation;
import it.unicam.hackhub.model.Mentor;
import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.repository.ViolationRepository;
import it.unicam.hackhub.repository.HackathonRepository;

import java.util.List;
import java.util.UUID;

public class ViolationService {
    private final ViolationRepository repo;
    private final StaffService staffService;
    private HackathonRepository hackathonRepo;
    private TeamService teamService;

    public ViolationService(ViolationRepository repo, StaffService staffService) {
        this.repo = repo;
        this.staffService = staffService;
    }

    public void setHackathonRepo(HackathonRepository hackathonRepo) {
        this.hackathonRepo = hackathonRepo;
    }

    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    public Violation createViolation(String hackathonId, String teamId, String reportedMemberId, String mentorId, String reason) {
        Mentor mentor = staffService.getMentor(mentorId);

        Violation violation = new Violation(
                UUID.randomUUID().toString(),
                hackathonId,
                teamId,
                reportedMemberId,
                mentor.getId(),
                reason
        );
        repo.save(violation);
        return violation;
    }

    public List<Violation> listViolations() {
        List<Violation> list = repo.findAll();
        if (list.isEmpty()) {
            System.out.println("SYSTEM: L'elenco delle segnalazioni è vuoto. Ritorno alla home.");
        }
        return list;
    }

    // UC Squalifica Team 
    public void handleDisqualifyTeam(String violationId) {
        Violation violation = repo.findById(violationId)
                .orElseThrow(() -> new IllegalStateException("Segnalazione non trovata"));
        
        if (hackathonRepo != null) {
            Hackathon h = hackathonRepo.findById(violation.getHackathonId()).orElse(null);
            Team t = teamService.getById(violation.getTeamId());
            if (h != null && t != null) {
                h.removeTeam(t);
                hackathonRepo.save(h);
            }
        }
        repo.delete(violationId); // Rimuove la segnalazione conclusa
    }

    // UC Squalifica Membro 
    public void handleDisqualifyMember(String violationId) {
        Violation violation = repo.findById(violationId)
                .orElseThrow(() -> new IllegalStateException("Segnalazione non trovata"));
        
        if (teamService != null && violation.getReportedMemberId() != null) {
            teamService.removeMember(violation.getReportedMemberId(), violation.getTeamId(), violation.getReportedMemberId());
        }
        repo.delete(violationId);
    }

    // UC Nessuna azione 
    public void handleNoAction(String violationId) {
        Violation violation = repo.findById(violationId)
                .orElseThrow(() -> new IllegalStateException("Segnalazione non trovata"));
        repo.delete(violationId);
    }
}