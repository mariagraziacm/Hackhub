package it.unicam.hackhub;

import it.unicam.hackhub.model.*;
import it.unicam.hackhub.repository.*;
import it.unicam.hackhub.service.*;
import it.unicam.hackhub.controller.*;

public class Main {
    public static void main(String[] args) {
        TeamRepository teamRepo = new TeamRepository();
        UserRepository userRepo = new UserRepository();
        HackathonRepository hackRepo = new HackathonRepository();

        InviteRepository inviteRepo = new InviteRepository();
        SubmissionRepository submissionRepo = new SubmissionRepository();
        ViolationRepository violationRepo = new ViolationRepository();

        TeamService teamService = new TeamService(teamRepo);
        HackathonService hackathonService = new HackathonService(hackRepo, teamService);
        InviteService inviteService = new InviteService(inviteRepo, teamService, userRepo);
        SubmissionService submissionService = new SubmissionService(submissionRepo);
        ViolationService violationService = new ViolationService(violationRepo, teamRepo);

        InviteController inviteController = new InviteController(inviteService);
        SubmissionController submissionController = new SubmissionController(submissionService);
        ViolationController violationController = new ViolationController(violationService);

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

        Team team = teamService.createTeam("T1", "TeamRocket", leader);

        System.out.println("Team creato: " + team.getName());
        System.out.println("Leader: " + team.getLeader().getUser().getName());

        team.addMember(new TeamMember("TM2", u2, Role.MEMBER));
        team.addMember(new TeamMember("TM3", u3, Role.MEMBER));

        System.out.println("Membri team: " + team.getMembers().size());

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

        hackathon.nextState();
        System.out.println("Nuovo stato: " + hackathon.getState().getName());

        try {
            Team team3 = teamService.createTeam("T3", "TeamBlocked", leader);
            hackathon.iscriviTeam(team3);
        } catch (Exception e) {
            System.out.println("Errore atteso (state block): " + e.getMessage());
        }

        System.out.println("Team finali nell'hackathon: " + hackathon.getTeams().size());

  
        // ITERAZIONE 2
        

        // Inviti mentor/judge
        try {
            inviteController.inviteMentor("H1", "U7");
            inviteController.inviteJudge("H1", "U8");
            System.out.println("Inviti salvati nel repository: " + inviteRepo.findAll().size());
        } catch (Exception e) {
            System.out.println("Errore inviti: " + e.getMessage());
        }

        // Invito team + accept
        try {
            User u9 = new User("Elena", "Test", "elena@mail.it", "123", "U9");
            userRepo.save(u9);

            inviteController.sendInvite("U1", "T1", "U9");
            Invite lastInvite = inviteRepo.findAll().get(inviteRepo.findAll().size() - 1);
            inviteController.acceptInvite(lastInvite.getId());

            System.out.println("Membri TeamRocket dopo accettazione U9: " + team.getMembers().size());
        } catch (Exception e) {
            System.out.println("Errore ciclo invito: " + e.getMessage());
        }

        // Submission send/edit
        try {
            submissionController.sendSubmission("H1", "T1", "Project Rocket", "Descrizione progetto");
            String submissionId = submissionRepo.findAll().get(0).getId();
            submissionController.editSubmission(submissionId, "Project Rocket v2", "Descrizione aggiornata");
            
            // Nota: Se qui scatta l'errore "Submission cannot be resolved", controlla l'import dentro SubmissionController.java!
            System.out.println("Titolo submission aggiornato in repository: " + submissionRepo.findAll().get(0).getTitle());
        } catch (Exception e) {
            System.out.println("Errore submission: " + e.getMessage());
        }

        // Violazione + no action
        try {
            // "H1" (hackathonId), "T1" (teamId), "U2" (reportedMemberId), "U7" (mentorId), "Ragione"
            violationService.createViolation("H1", "T1", "U2", "U7", "Comportamento scorretto");
            String violationId = violationRepo.findAll().get(0).getId();
            violationController.chooseNoAction(violationId);
            
            System.out.println("Violazione registrata per il team: " + violationRepo.findAll().get(0).getTeamId());
        } catch (Exception e) {
            System.out.println("Errore violazione: " + e.getMessage());
        }

        // Team UC
        try {
            teamService.changeLeader("U1", "T1", "U2");
            System.out.println("Nuovo leader effettivo di T1: " + team.getLeader().getUser().getName());

            teamService.removeMember("U2", "T1", "U3");
            System.out.println("Membri T1 dopo rimozione U3: " + team.getMembers().size());

            teamService.leaveTeam("T1", "U9");
            System.out.println("Membri T1 dopo abbandono U9: " + team.getMembers().size());
        } catch (Exception e) {
            System.out.println("Errore use cases team: " + e.getMessage());
        }

        System.out.println("=== FINE TEST ===");
    }
}