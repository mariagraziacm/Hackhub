package it.unicam.hackhub.service;

import it.unicam.hackhub.model.User;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.TeamMember;
import it.unicam.hackhub.model.ParticipationRequest;
import it.unicam.hackhub.repository.ParticipationRequestRepository;
import it.unicam.hackhub.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ParticipationRequestService {
    private final ParticipationRequestRepository repo;
    private final TeamService teamService;
    private final UserRepository userRepo;

    public ParticipationRequestService(
            ParticipationRequestRepository repo,
            TeamService teamService,
            UserRepository userRepo) {

        this.repo = repo;
        this.teamService = teamService;
        this.userRepo = userRepo;
    }

   
    @Transactional
    public ParticipationRequest sendRequest(String teamId, String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User non trovato"));
        
        Team team = teamService.getById(teamId);
        
        if (team.isFull()) {
            throw new IllegalStateException("Team pieno");
        }
        
        if (teamService.isUserInAnyTeam(userId)) {
            throw new IllegalStateException("Utente già in un team");
        }

        
        boolean alreadyRequested = repo.existsByUserIdAndTeamIdAndState(
                userId, 
                teamId, 
                ParticipationRequest.ParticipationRequestState.PENDING
        );

        if (alreadyRequested) {
            throw new IllegalStateException("Richiesta di partecipazione già existent");
        }

        ParticipationRequest req = new ParticipationRequest(
                UUID.randomUUID().toString(),
                user,
                team
        );
        
        repo.save(req);
        return req;
    }

    // SE leader accetta la richiesta
    @Transactional
    public void acceptRequest(String requestId, String leaderId) {
        ParticipationRequest req = repo.findById(requestId)
                .orElseThrow(() -> new IllegalStateException("Richiesta non trovata"));

        Team team = req.getTeam();

        // Solo leader del team può accettare
        if (team.getLeader() == null || !team.getLeader().getUserId().equals(leaderId)) {
            throw new IllegalStateException("Solo il leader del team può accettare le richieste di partecipazione");
        }

        if (team.isFull()) {
            throw new IllegalStateException("Il team è pieno");
        }

        if (teamService.isUserInAnyTeam(req.getUser().getId())) {
            throw new IllegalStateException("L'utente fa già parte di un team");
        }

        // Cambia lo stato della richiesta in ACCEPTED
        req.accept();

        // Aggiunge l'utente al team usando enum 
        team.addMember(new TeamMember(
                UUID.randomUUID().toString(),
                req.getUser(),
                TeamMember.Role.MEMBER
        ));
        
       
        repo.save(req);
    }

    // SE leader del team rifiuta richiesta di partecipazione
    @Transactional
    public void declineRequest(String requestId, String leaderId) {
        ParticipationRequest req = repo.findById(requestId)
                .orElseThrow(() -> new IllegalStateException("Richiesta non trovata"));

        Team team = req.getTeam();

        // Solo il leader del team può rifiutare
        if (team.getLeader() == null || !team.getLeader().getUserId().equals(leaderId)) {
            throw new IllegalStateException("Solo il leader del team può rifiutare le richieste di partecipazione");
        }

        // Cambia lo stato della richiesta in DECLINED
        req.decline();
        
      
        repo.save(req);
    }
}