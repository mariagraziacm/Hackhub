package it.unicam.hackhub;

import it.unicam.hackhub.controller.PartecipationController;
import it.unicam.hackhub.model.PartecipationRequest;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.model.TeamMember;
import it.unicam.hackhub.repository.TeamRepository;
import it.unicam.hackhub.repository.UsersRepository;

public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("          STARTING HACKHUB SIMULATION             ");
        System.out.println("==================================================\n");

        // 1. INIZIALIZZAZIONE REPOSITORY
        UsersRepository usersRepository = new UsersRepository();
        TeamRepository teamRepository = new TeamRepository();
        HackathonRepository hackathonRepository = new HackathonRepository();

        // 2. INIZIALIZZAZIONE SERVIZI
        HackathonService hackathonService = new HackathonService(hackathonRepository);
        TeamService teamService = new TeamService(teamRepository);
        PartecipationRequestService requestService = new PartecipationRequestService(teamRepository, usersRepository);

        // 3. INIZIALIZZAZIONE CONTROLLER
        HackathonController hackathonController = new HackathonController(hackathonService);
        IscrizioneController iscrizioneController = new IscrizioneController(teamService, hackathonService);
        PartecipationController partecipationController = new PartecipationController(requestService);
        InviteController inviteController = new InviteController(teamRepository, usersRepository);

        // 4. POPOLAMENTO UTENTI (Simuliamo database iniziale)
        User alice = new User("USR-1", "Alice");
        User bob = new User("USR-2", "Bob");
        User charlie = new User("USR-3", "Charlie");
        User david = new User("USR-4", "David");

        usersRepository.save(alice);
        usersRepository.save(bob);
        usersRepository.save(charlie);
        usersRepository.save(david);

        // ==================================================
        // SCENARIO 1: CREAZIONE HACKATHON
        // ==================================================
        System.out.println("--- Scenario 1: Creazione Hackathon ---");
        hackathonController.newHackathon("HACK-2026", "AI Innovation Challenge", 100, "Sviluppo modelli AI");

        // Test duplicato (Deve fallire)
        hackathonController.newHackathon("HACK-DUP", "AI Innovation Challenge", 50, "Duplicato");
        System.out.println();

        // ==================================================
        // SCENARIO 2: CREAZIONE TEAM E CONTROLLI UNICITÀ
        // ==================================================
        System.out.println("--- Scenario 2: Creazione Team ---");
        Team teamAlpha = iscrizioneController.creaTeam("TEAM-A", "AlphaCoders", alice); // Alice è leader

        // Test utente già in un altro team (Deve fallire)
        iscrizioneController.creaTeam("TEAM-B", "BetaTesters", alice);
        System.out.println();

        // ==================================================
        // SCENARIO 3: TENTATIVO ISCRIZIONE FALLITO (Meno di 2 membri)
        // ==================================================
        System.out.println("--- Scenario 3: Test Vincolo Minimo Partecipanti ---");
        System.out.println("Tentativo di iscrivere il team Alpha con 1 solo membro (Solo Alice)...");
        iscrizioneController.iscriviTeamAdHackathon("TEAM-A", "HACK-2026");
        System.out.println();

        // ==================================================
        // SCENARIO 4: AGGIUNTA MEMBRI (Richiesta & Invito)
        // ==================================================
        System.out.println("--- Scenario 4: Ampliamento Team ---");

        // Bob manda una richiesta di partecipazione ad AlphaCoders
        partecipationController.sendRequest("TEAM-A", "USR-2");
        // Il service accetta la richiesta interna REQ-1 (Bob entra nel team)
        requestService.acceptRequest("REQ-1");
        System.out.println("SYSTEM: Bob è entrato nel team.");

        // Il leader Alice invita Charlie usando l'InviteController
        inviteController.sendInvite("USR-1", "TEAM-A", "USR-3");
        System.out.println();

        // ==================================================
        // SCENARIO 5: ISCRIZIONE CON SUCCESSO
        // ==================================================
        System.out.println("--- Scenario 5: Iscrizione con requisiti validi ---");
        System.out.println("Ora il team ha " + teamRepository.findById("TEAM-A").get().getMembers().size() + " membri.");
        iscrizioneController.iscriviTeamAdHackathon("TEAM-A", "HACK-2026");
        System.out.println();

        // ==================================================
        // SCENARIO 6: CAMBIO STATO E BLOCCO ABBANDONO
        // ==================================================
        System.out.println("--- Scenario 6: Avanzamento Stato e Test Vincoli ---");

        // L'amministratore fa avanzare l'Hackathon allo stato IN_CORSO
        hackathonService.advanceState("HACK-2026");

        // Ora che è IN_CORSO, il leader Alice prova ad abbandonare (Deve fallire per via dello State Pattern)
        System.out.println("Tentativo di abbandono in fase IN_CORSO...");
        iscrizioneController.abbandonaHackathon("USR-1", "TEAM-A", "HACK-2026");

        System.out.println("\n==================================================");
        System.out.println("          SIMULATION ENDED SUCCESSFULLY           ");
        System.out.println("==================================================");
    }
}