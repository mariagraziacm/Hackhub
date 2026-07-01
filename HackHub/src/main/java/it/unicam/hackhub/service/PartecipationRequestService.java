package main.java.it.unicam.hackhub.service;

import it.unicam.hackhub.repository.UsersRepository;
import it.unicam.hackhub.repository.TeamRepository;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.TeamMember;
import it.unicam.hackhub.model.PartecipationRequest;


import java.util.ArrayList;
import java.util.List;

public class PartecipationRequestService {
    private final TeamRepository teamRepository;
    private final UsersRepository usersRepository;

    private final List<PartecipationRequestService> requests = new ArrayList<>();

    public PartecipationRequestService(
            TeamRepository teamRepository,
            UsersRepository usersRepository
    ) {
        this.teamRepository = teamRepository;
        this.usersRepository = usersRepository;
    }


    public PartecipationRequestService sendRequest(String teamId, String userId) {

        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User non trovato"));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalStateException("Team non trovato"));

        if (team.getMembers().size() >= 5) {
            throw new IllegalStateException("Team pieno");
        }

        PartecipationRequestService request = new PartecipationRequestService(
                "REQ-" + (requests.size() + 1), user, team);

        requests.add(request);

        return request;
    }

    public void acceptRequest(String requestId) {
        PartecipationRequestService req = findRequest(requestId);
        req.accept();

        Team team = req.getTeam();

        TeamMember member = new TeamMember(
                "TM-" + req.getId(),
                team.getId(),
                null,
                req.getUser()
        );

        team.getMembers().add(member);
    }

    public void declineRequest(String requestId) {
        findRequest(requestId).decline();
    }

    private PartecipationRequestService findRequest(String id) {
        return requests.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Request non trovata"));
    }

    public List<PartecipationRequestService> getRequests() {
        return requests;
    }
}
