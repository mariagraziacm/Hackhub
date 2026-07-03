package it.unicam.hackhub.service;

import it.unicam.hackhub.model.*;
import it.unicam.hackhub.repository.CallRepository;
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
    private final CallRepository callRepo;

    public SupportRequestService(SupportRequestRepository repo,
                                 TeamService teamService,
                                 StaffService staffService,
                                 HackathonRepository hackathonRepo,
                                 CallRepository callRepo) {
        this.repo = repo;
        this.teamService = teamService;
        this.staffService = staffService;
        this.hackathonRepo = hackathonRepo;
        this.callRepo = callRepo;
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

        Mentor mentor = staffService.getMentor(mentorId);

        Hackathon hackathon = hackathonRepo.findById(mentor.getHackathonId())
                .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));

        if (!(hackathon.getState() instanceof InCorsoState)) {
            throw new IllegalStateException("Le richieste sono visibili solo in hackathon IN CORSO");
        }

        return repo.findByMentorId(mentorId);
    }
    public Call planCall(String mentorId, String requestId, String slotId) {

        Mentor mentor = staffService.getMentor(mentorId);

        SupportRequest request = repo.findById(requestId)
                .orElseThrow(() -> new IllegalStateException("Request non trovata"));

        Hackathon hackathon = hackathonRepo.findById(request.getHackathonId())
                .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));

        if (!(hackathon.getState() instanceof InCorsoState)) {
            throw new IllegalStateException("Call consentite solo in IN CORSO");
        }

        if (!request.getMentorId().equals(mentor.getId())) {
            throw new IllegalStateException("Mentor non autorizzato");
        }

        // crea call
        Call call = new Call(
                UUID.randomUUID().toString(),
                mentor.getId(),
                request.getTeamId(),
                request.getHackathonId(),
                slotId
        );

        call.setWaitingResponse();

        // collega request ↔ call
        request.setCallId(call.getId());

        callRepo.save(call);
        repo.save(request);

        return call;
    }
}