package it.unicam.hackhub.service;

import it.unicam.hackhub.model.User;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.TeamMember;
import it.unicam.hackhub.model.ParticipationRequest;
import it.unicam.hackhub.model.Role;
import it.unicam.hackhub.repository.ParticipationRequestRepository;
import it.unicam.hackhub.repository.UserRepository;

import java.util.UUID;

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

    // L'utente invia una richiesta per unirsi a un Team
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

        // Verifica se esiste già una richiesta in sospeso identica
        boolean alreadyRequested = repo.findAll().stream()
                .anyMatch(r ->
                        r.getUser().getId().equals(userId)
                                && r.getTeam().getId().equals(teamId)
                                && r.getState() == ParticipationRequest.ParticipationRequestState.PENDING
                );

        if (alreadyRequested) {
            throw new IllegalStateException("Richiesta di partecipazione già esistente");
        }

        ParticipationRequest req = new ParticipationRequest(
                UUID.randomUUID().toString(),
                user,
                team
        );
        
        repo.save(req);
        return req;
    }

    // Il leader del team accetta la richiesta
    public void acceptRequest(String requestId, String leaderId) {
        ParticipationRequest req = repo.findById(requestId)
                .orElseThrow(() -> new IllegalStateException("Richiesta non trovata"));

        Team team = req.getTeam();

        // Solo il leader del team destinatario può accettare
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

        // Aggiunge l'utente al team 
        team.addMember(new TeamMember(
                UUID.randomUUID().toString(),
                req.getUser(),
                Role.MEMBER
        ));
        
        // Salva lo stato modificato della richiesta nel repository
        repo.save(req);
    }

    // Il leader del team rifiuta la richiesta di partecipazione
    public void declineRequest(String requestId, String leaderId) {
        ParticipationRequest req = repo.findById(requestId)
                .orElseThrow(() -> new IllegalStateException("Richiesta non trovata"));

        Team team = req.getTeam();

        // Solo il leader del team destinatario può rifiutare
        if (team.getLeader() == null || !team.getLeader().getUserId().equals(leaderId)) {
            throw new IllegalStateException("Solo il leader del team può rifiutare le richieste di partecipazione");
        }

        // Cambia lo stato della richiesta in DECLINED
        req.decline();
        
        // Salva lo stato modificato nel repository
        repo.save(req);
    }
}