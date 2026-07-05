package it.unicam.hackhub.service;

import it.unicam.hackhub.model.Violation;
import it.unicam.hackhub.model.Mentor;
import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.repository.ViolationRepository;
import it.unicam.hackhub.repository.HackathonRepository;
import it.unicam.hackhub.state.InCorsoState;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ViolationService {
    
    private final ViolationRepository repo;
    private final StaffService staffService;
    private final HackathonRepository hackathonRepo;
    private final TeamService teamService;

    
    public ViolationService(ViolationRepository repo, 
                            StaffService staffService, 
                            HackathonRepository hackathonRepo, 
                            TeamService teamService) {
        this.repo = repo;
        this.staffService = staffService;
        this.hackathonRepo = hackathonRepo;
        this.teamService = teamService;
    }

    public Violation createViolation(String hackathonId, String teamId, String reportedMemberId, String mentorId, String reason) {

        Mentor mentor = staffService.getMentor(mentorId);

        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));

        if (!(hackathon.getState() instanceof InCorsoState)) {
            throw new IllegalStateException("Violazioni consentite solo in hackathon IN CORSO");
        }

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

    @Transactional 
    public void resolveViolation(String violationId, Violation.ViolationStatus status) {
        Violation violation = repo.findById(violationId)
                .orElseThrow(() -> new IllegalStateException("Segnalazione non trovata"));

        Hackathon hackathon = hackathonRepo.findById(violation.getHackathonId())
                .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));

        violation.resolve(status);

        switch (status) {
            case DISQUALIFY_TEAM -> {
                Team team = teamService.getById(violation.getTeamId());
                hackathon.removeTeam(team);
            }

            case DISQUALIFY_MEMBER -> {
                teamService.removeMember(
                        violation.getReportedMemberId(),
                        violation.getTeamId(),
                        violation.getReportedMemberId()
                );
            }

            case NO_ACTION -> {
              
            }

            default -> throw new IllegalStateException("Stato non valido");
        }

        repo.save(violation);
        hackathonRepo.save(hackathon);
    }
}