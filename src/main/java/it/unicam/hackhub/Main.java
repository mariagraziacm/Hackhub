package it.unicam.hackhub;

import it.unicam.hackhub.controller.SupportRequestController;
import it.unicam.hackhub.model.*;

import it.unicam.hackhub.repository.*;

import it.unicam.hackhub.service.*;

import it.unicam.hackhub.controller.InviteController;
import it.unicam.hackhub.controller.SubmissionController;
import it.unicam.hackhub.controller.ViolationController;
import it.unicam.hackhub.state.InValutazioneState;

public class Main {
    public static void main(String[] args) {


        TeamRepository teamRepo = new TeamRepository();
        UserRepository userRepo = new UserRepository();
        HackathonRepository hackRepo = new HackathonRepository();
        InviteRepository inviteRepo = new InviteRepository();
        SubmissionRepository submissionRepo = new SubmissionRepository();
        ViolationRepository violationRepo = new ViolationRepository();
        StaffRepository staffRepository = new StaffRepository();
        CallRepository callRepo = new CallRepository(); 


        StaffService staffService = new StaffService(staffRepository);
        TeamService teamService = new TeamService(teamRepo);
        HackathonService hackathonService = new HackathonService(hackRepo, teamService, staffService, submissionRepo);
        InviteService inviteService = new InviteService(inviteRepo, teamService, userRepo, staffRepository);
        SubmissionService submissionService = new SubmissionService(submissionRepo, hackRepo, teamRepo, staffService);
        ViolationService violationService = new ViolationService(violationRepo, staffService);

        violationService.setHackathonRepo(hackRepo);
        violationService.setTeamService(teamService);


        InviteController inviteController = new InviteController(inviteService);
        SubmissionController submissionController = new SubmissionController(submissionService);
        ViolationController violationController = new ViolationController(violationService);

       
        User leader = new User("Mario", "Rossi", "mario_user", "mario@mail.it", "123", "U1", null);
        User u2 = new User("Luca", "Bianchi", "luca_user", "luca@mail.it", "123", "U2", null);
        User u3 = new User("Anna", "Verdi", "anna_user", "anna@mail.it", "123", "U3", null);
        User mentor = new User("Paolo", "Mentor", "mentor_user", "mentor@mail.it", "123", "U7", null);
        User judge = new User("Giulia", "Judge", "judge_user", "judge@mail.it", "123", "U8", null);
        User uOrganizer = new User("Stefano", "Organizer", "org_user", "org@mail.it", "123", "U10", null);

        userRepo.save(leader);
        userRepo.save(u2);
        userRepo.save(u3);
        userRepo.save(mentor);
        userRepo.save(judge);
        userRepo.save(uOrganizer);

        Organizer organizer = new Organizer("ORG1", uOrganizer, "Hfsdfsd1");
        Mentor mentorStaff = new Mentor("MNT1", mentor, "H1");
        staffRepository.save(organizer);
        staffRepository.save(mentorStaff);

        // Test Iterazione 1
        Team team = teamService.createTeam("T1", "TeamRocket", leader);

        System.out.println("Team creato: " + team.getName());
        System.out.println("Leader: " + team.getLeader().getUser().getName());

        team.addMember(new TeamMember("TM2", u2, TeamMember.Role.MEMBER));

        System.out.println("Membri team: " + team.getMembers().size());

        Hackathon hackathon = hackathonService.createHackathon("H1", "Hackathon Test", "a", "ORG1");

        System.out.println("Hackathon creato: " + hackathon.getName());
        System.out.println("Stato iniziale: " + hackathon.getState().getName());

        hackathon.iscriviTeam(team);
        System.out.println("Team iscritti: " + hackathon.getTeams().size());

        User leader2 = new User("Giulia", "Neri", "giulia_user", "giulia@mail.it", "123", "U4", null);
        userRepo.save(leader2);

        Team team2 = teamService.createTeam("T2", "TeamBlue", leader2);
        User marco = new User("Marco", "Rossi", "marco_user", "m", "p", "U5", null);
        User sara = new User("Sara", "Verdi", "sara_user", "s", "p", "U6", null);

        userRepo.save(marco);
        userRepo.save(sara);

        team2.addMember(new TeamMember("TM4", marco, TeamMember.Role.MEMBER));
        team2.addMember(new TeamMember("TM5", sara, TeamMember.Role.MEMBER));
        hackathon.iscriviTeam(team2);
        System.out.println("Team iscritti dopo secondo team: " + hackathon.getTeams().size());

        hackathon.nextState();
        hackRepo.save(hackathon);
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
            User u9 = new User("Elena", "Test", "elena_user", "elena@mail.it", "123", "U9", null);
            userRepo.save(u9);

            inviteController.sendInvite("U1", "T1", "U9");
            Invite lastInvite = inviteRepo.findAll().get(inviteRepo.findAll().size() - 1);
            inviteController.acceptInvite(lastInvite.getId());

            team = teamService.getById("T1");
            System.out.println("Membri TeamRocket dopo accettazione U9: " + team.getMembers().size());
        } catch (Exception e) {
            System.out.println("Errore ciclo invito: " + e.getMessage());
        }

        // UC: Sottomissione Progetto
        try {
            submissionController.sendSubmission("H1", "T1", "Project Rocket", "Descrizione progetto");
            String submissionId = submissionRepo.findAll().get(0).getId();
            submissionController.editSubmission(submissionId, "Project Rocket v2", "Descrizione aggiornata");

            System.out.println("Titolo submission aggiornato in repository: " + submissionRepo.findAll().get(0).getTitle());
        } catch (Exception e) {
            System.out.println("Errore submission: " + e.getMessage());
        }

        // UC: Violazioni con gestione dell'Organizzatore
        try {
            violationService.createViolation("H1", "T1", "U2", "MNT1", "Comportamento scorretto");
            String violationId = violationRepo.findAll().get(0).getId();
            violationController.resolveViolation(
                    violationId,
                    Violation.ViolationStatus.PENDING
            );

            System.out.println("Violazione registrata per il team: " + violationRepo.findAll().size());
        } catch (Exception e) {
            System.out.println("Errore violazione: " + e.getMessage());
        }

        // UC: (Cambio Leader, Rimozione, Abbandono)
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

        System.out.println("\n--- UC: SUPPORT REQUEST ---");

        try {
            SupportRequestController supportController =
                    new SupportRequestController(
                            new SupportRequestService(
                                    new SupportRequestRepository(),
                                    teamService,
                                    staffService,
                                    hackRepo,
                                    callRepo 
                            )
                    );

            supportController.sendSupportRequest(
                    "T1",
                    "U2",
                    "MNT1",
                    "H1",
                    "Ho bisogno di aiuto sul progetto"
            );

            System.out.println("Support requests create: 1");

        } catch (Exception e) {
            System.out.println("Errore support request: " + e.getMessage());
        }

        System.out.println("\n--- UC: PROCLAMA VINCITORE ---");

        try {
            if (!(hackathon.getState() instanceof InValutazioneState)) {
                hackathon.nextState();
            }

            System.out.println("Stato attuale: " + hackathon.getState().getName());

            hackathon.proclamaVincitore(team2);

            System.out.println("🏆 Vincitore: " + hackathon.getWinner().getName());
            System.out.println("Stato finale: " + hackathon.getState().getName());

        } catch (Exception e) {
            System.out.println("Errore proclamazione vincitore: " + e.getMessage());
        }

        System.out.println("\n--- ITERAZIONE TRE ---");
        System.out.println("\n--- UC: VALUTA SOTTOMISSIONE ---");

        try {
            hackathon.valutaSottomissione(
                    team,
                    submissionRepo.findAll().get(0),
                    9,
                    "Ottimo lavoro"
            );

            System.out.println("Valutazione completata per team: " + team.getName());

        } catch (Exception e) {
            System.out.println("Errore valutazione: " + e.getMessage());
        }
    }
}