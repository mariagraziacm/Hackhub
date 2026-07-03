package it.unicam.hackhub.service;

import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.Mentor;
import it.unicam.hackhub.model.SupportRequest;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.repository.HackathonRepository;
import it.unicam.hackhub.repository.SupportRequestRepository;
import it.unicam.hackhub.state.InCorsoState;

import java.util.List;
import java.util.UUID;

public class SupportRequestService {
    private final SupportRequestRepository repo;
    private final TeamService teamService;
    private final StaffService staffService;
    private final HackathonRepository hackathonRepo;

    public SupportRequestService(SupportRequestRepository repo,
                                 TeamService teamService,
                                 StaffService staffService,
                                 HackathonRepository hackathonRepo) {
        this.repo = repo;
        this.teamService = teamService;
        this.staffService = staffService;
        this.hackathonRepo = hackathonRepo;
    }

    public SupportRequest sendRequest(String teamId,
                                      String memberId,
                                      String mentorId,
                                      String hackathonId,
                                      String message) {

        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));

        if (!(hackathon.getState() instanceof InCorsoState)) {
            throw new IllegalStateException("Richiesta supporto consentita solo in hackathon IN CORSO");
        }

        Team team = teamService.getById(teamId);

        if (!team.hasUser(memberId)) {
            throw new IllegalStateException("L'utente non appartiene al team");
        }

        Mentor mentor = staffService.getMentor(mentorId);

        SupportRequest request = new SupportRequest(
                UUID.randomUUID().toString(),
                hackathonId,
                team.getId(),
                mentor.getId(),
                message
        );

        repo.save(request);
        return request;
    }

    public List<SupportRequest> getMentorRequests(String mentorId) {
        return repo.findByMentorId(mentorId);
    }
}
