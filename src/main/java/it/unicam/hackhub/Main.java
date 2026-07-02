package it.unicam.hackhub;

import it.unicam.hackhub.model.*;
import it.unicam.hackhub.repository.*;
import it.unicam.hackhub.service.*;
import it.unicam.hackhub.controller.*;

public class Main {
    public static void main(String[] args) {
        // 1. Inizializzazione Repository
        TeamRepository teamRepo = new TeamRepository();
        UserRepository userRepo = new UserRepository();
        HackathonRepository hackRepo = new HackathonRepository();
        InviteRepository inviteRepo = new InviteRepository();
        SubmissionRepository submissionRepo = new SubmissionRepository();
        ViolationRepository violationRepo = new ViolationRepository();

        // 2. Inizializzazione Service (Sistemati i passaggi dei Repository corretti)
        TeamService teamService = new TeamService(teamRepo);
        HackathonService hackathonService = new HackathonService(hackRepo, teamService);
        InviteService inviteService = new InviteService(inviteRepo, teamService, userRepo);
        
        // Passiamo i repository necessari a SubmissionService per i controlli di Stato
        SubmissionService submissionService = new SubmissionService(submissionRepo, hackRepo, teamRepo);
        
        // Il costruttore di ViolationService richiede solo il suo repository
        ViolationService violationService = new ViolationService(violationRepo);

        // 3. Inizializzazione Controller
        InviteController inviteController = new InviteController(inviteService);
        SubmissionController submissionController = new SubmissionController(submissionService);
        ViolationController violationController = new ViolationController(violationService);

        // 4. Creazione Utenti di Test
        User leader = new User("Mario", "Rossi", "mario@mail.it", "123", "U1");
        User u2 = new User("Luca", "Bianchi", "luca@mail.it", "123", "U2");
        User u3 = new User("Anna", "Verdi", "anna@mail.it", "123", "U3");
        User mentor = new User("Paolo", "Mentor", "mentor@mail.it", "123", "U7");
        User judge = new User("Giulia", "Judge", "judge@mail.it", "123", "U8");

        userRepo.save(leader);
        userRepo.save(u2);
        userRepo.save(u3);
        userRepo.save(mentor);
        userRepo.save(judge);

        // 5. Test Iterazione 1: Creazione e Gestione Team
        Team team = teamService.createTeam("T1", "TeamRocket", leader);

        System.out.println("Team creato: " + team.getName());
        System.out.println("Leader: " + team.getLeader().getUser().getName());

        team.addMember(new TeamMember("TM2", u2, Role.MEMBER));
        team.addMember(new TeamMember("TM3", u3, Role.MEMBER));

        System.out.println("Membri team: " + team.getMembers().size());

        // 6. Test State Pattern su Hackathon
        Hackathon hackathon = hackathonService.createHackathon("H1", "Hackathon Test", "a");

        System.out.println("Hackathon creato: " + hackathon.getName());
        System.out.println("Stato iniziale: " + hackathon.getState().getName());

        hackathon.iscriviTeam(team);
        System.out.println("Team iscritti: " + hackathon.getTeams().size());

        User leader2 = new User("Giulia", "Neri", "giulia@mail.it", "123", "U4");
        userRepo.save(leader2);

        Team team2 = teamService.createTeam("T2", "TeamBlue", leader2);
        team2.addMember(new TeamMember("TM4", new User("Marco", "Rossi", "m", "p", "U5"), Role.MEMBER));
        team2.addMember(new TeamMember("TM5", new User("Sara", "Verdi", "s", "p", "U6"), Role.MEMBER));

        hackathon.iscriviTeam(team2);
        System.out.println("Team iscritti dopo secondo team: " + hackathon.getTeams().size());

        // Avanzamento di stato: passa a IN_CORSO
        hackathon.nextState();
        hackRepo.save(hackathon); // Aggiorna lo stato nel repository simulato
        System.out.println("Nuovo stato: " + hackathon.getState().getName());

        try {
            Team team3 = teamService.createTeam("T3", "TeamBlocked", leader);
            hackathon.iscriviTeam(team3);
        } catch (Exception e) {
            System.out.println("Errore atteso (state block): " + e.getMessage());
        }

        System.out.println("Team finali nell'hackathon: " + hackathon.getTeams().size());

        System.out.println("\n--- ITERAZIONE 2 ---");

        // UC: Inviti Mentor/Judge
        try {
            inviteController.inviteMentor("H1", "U7");
            inviteController.inviteJudge("H1", "U8");
            System.out.println("Inviti salvati nel repository: " + inviteRepo.findAll().size());
        } catch (Exception e) {
            System.out.println("Errore inviti: " + e.getMessage());
        }

        // UC: Invito membro nel Team + Accettazione
        try {
            User u9 = new User("Elena", "Test", "elena@mail.it", "123", "U9");
            userRepo.save(u9);

            inviteController.sendInvite("U1", "T1", "U9");
            Invite lastInvite = inviteRepo.findAll().get(inviteRepo.findAll().size() - 1);
            inviteController.acceptInvite(lastInvite.getId());

            team = teamService.getById("T1"); // Sincronizza l'oggetto
            System.out.println("Membri TeamRocket dopo accettazione U9: " + team.getMembers().size());
        } catch (Exception e) {
            System.out.println("Errore ciclo invito: " + e.getMessage());
        }

        // UC: Sottomissione Progetto (Send / Edit)
        try {
            // Usiamo il controller per inviare la sottomissione in modo coerente
            submissionController.sendSubmission("H1", "T1", "Project Rocket", "Descrizione progetto");
            String submissionId = submissionRepo.findAll().get(0).getId();
            submissionController.editSubmission(submissionId, "Project Rocket v2", "Descrizione aggiornata");
            
            System.out.println("Titolo submission aggiornato in repository: " + submissionRepo.findAll().get(0).getTitle());
        } catch (Exception e) {
            System.out.println("Errore submission: " + e.getMessage());
        }

        // UC: Violazioni con gestione dell'Organizzatore
        try {
            violationService.createViolation("H1", "T1", "U2", "U7", "Comportamento scorretto");
            String violationId = violationRepo.findAll().get(0).getId();
            violationController.chooseNoAction(violationId);
            
            System.out.println("Violazione registrata per il team: " + violationRepo.findAll().get(0).getTeamId());
        } catch (Exception e) {
            System.out.println("Errore violazione: " + e.getMessage());
        }

        // UC: Operazioni interne del Team (Cambio Leader, Rimozione, Abbandono)
        try {
            teamService.changeLeader("U1", "T1", "U2");
            team = teamService.getById("T1");
            System.out.println("Nuovo leader effettivo di T1: " + team.getLeader().getUser().getName());
            
            teamService.removeMember("U2", "T1", "U3");
            team = teamService.getById("T1");
            System.out.println("Membri T1 dopo rimozione U3: " + team.getMembers().size());

            teamService.leaveTeam("T1", "U9");
            team = teamService.getById("T1");
            System.out.println("Membri T1 dopo abbandono U9: " + team.getMembers().size());
        } catch (Exception e) {
            System.out.print("Errore use cases team: ");
            e.printStackTrace();
        }

        System.out.println("=== FINE TEST ===");
    }
}