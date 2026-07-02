package it.unicam.hackhub.service;

import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.TeamMember;
import it.unicam.hackhub.model.Violation;
import it.unicam.hackhub.repository.TeamRepository;
import it.unicam.hackhub.repository.ViolationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ViolationService {

    private final ViolationRepository violationRepo;
    private final TeamRepository teamRepo;

    public ViolationService(ViolationRepository violationRepo,
                            TeamRepository teamRepo) {
        this.violationRepo = violationRepo;
        this.teamRepo = teamRepo;
    }

    public List<Violation> listPendingViolations() {
        return violationRepo.findPending();
    }

    public Violation getById(String violationId) {
        return violationRepo.findById(violationId)
                .orElseThrow(() -> new IllegalStateException("Violazione non trovata"));
    }

    // Creazione segnalazione (mentor -> organizer)
    public Violation createViolation(String hackathonId,
                                     String teamId,
                                     String reportedMemberId,
                                     String mentorId,
                                     String reason) {

        if (hackathonId == null || hackathonId.isBlank()) {
            throw new IllegalStateException("hackathonId obbligatorio");
        }
        if (teamId == null || teamId.isBlank()) {
            throw new IllegalStateException("teamId obbligatorio");
        }
        if (mentorId == null || mentorId.isBlank()) {
            throw new IllegalStateException("mentorId obbligatorio");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalStateException("Motivazione obbligatoria");
        }

        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new IllegalStateException("Team non trovato"));

        // opzionale: se indicato, deve essere membro del team
        if (reportedMemberId != null && !reportedMemberId.isBlank() && !team.hasUser(reportedMemberId)) {
            throw new IllegalStateException("Membro segnalato non presente nel team");
        }

        Violation violation = new Violation(
                UUID.randomUUID().toString(),
                hackathonId,
                teamId,
                reportedMemberId,
                mentorId,
                reason
        );

        violationRepo.save(violation);
        return violation;
    }

    // Scenario: Squalifica Team
    public void disqualifyTeam(String violationId) {
        Violation violation = getById(violationId);

        Team team = teamRepo.findById(violation.getTeamId())
                .orElseThrow(() -> new IllegalStateException("Team non trovato"));

        List<TeamMember> membersCopy = new ArrayList<>(team.getMembers());
        for (TeamMember member : membersCopy) {
            team.removeMember(member.getUserId());
        }

        violation.setDisqualifyTeam();
    }

    // Scenario: Squalifica Membro
    public void disqualifyMember(String violationId, String memberId) {
        if (memberId == null || memberId.isBlank()) {
            throw new IllegalStateException("memberId obbligatorio");
        }

        Violation violation = getById(violationId);

        Team team = teamRepo.findById(violation.getTeamId())
                .orElseThrow(() -> new IllegalStateException("Team non trovato"));

        if (!team.hasUser(memberId)) {
            throw new IllegalStateException("Membro non presente nel team");
        }

        if (team.isLeader(memberId)) {
            throw new IllegalStateException("Impossibile squalificare il leader con questa operazione");
        }

        team.removeMember(memberId);
        violation.setDisqualifyMember();
    }

    // Scenario: Nessuna Azione
    public void noAction(String violationId) {
        Violation violation = getById(violationId);
        violation.setNoAction();
        // alternativa:
        // violationRepo.deleteById(violationId);
    }
}