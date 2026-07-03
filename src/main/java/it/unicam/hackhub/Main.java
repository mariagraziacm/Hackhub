package it.unicam.hackhub;

import it.unicam.hackhub.model.User;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.TeamMember;
import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.Invite;
import it.unicam.hackhub.model.Organizer;
import it.unicam.hackhub.model.Mentor;

import it.unicam.hackhub.repository.TeamRepository;
import it.unicam.hackhub.repository.UserRepository;
import it.unicam.hackhub.repository.HackathonRepository;
import it.unicam.hackhub.repository.InviteRepository;
import it.unicam.hackhub.repository.SubmissionRepository;
import it.unicam.hackhub.repository.ViolationRepository;
import it.unicam.hackhub.repository.StaffRepository;

import it.unicam.hackhub.service.StaffService;
import it.unicam.hackhub.service.TeamService;
import it.unicam.hackhub.service.HackathonService;
import it.unicam.hackhub.service.InviteService;
import it.unicam.hackhub.service.SubmissionService;
import it.unicam.hackhub.service.ViolationService;

import it.unicam.hackhub.controller.InviteController;
import it.unicam.hackhub.controller.SubmissionController;
import it.unicam.hackhub.controller.ViolationController;

public class Main {
    public static void main(String[] args) {
    
        TeamRepository teamRepo = new TeamRepository();
        UserRepository userRepo = new UserRepository();
        HackathonRepository hackRepo = new HackathonRepository();
        InviteRepository inviteRepo = new InviteRepository();
        SubmissionRepository submissionRepo = new SubmissionRepository();
        ViolationRepository violationRepo = new ViolationRepository();
        StaffRepository staffRepository = new StaffRepository(); 
        
        StaffService staffService = new StaffService(staffRepository); 
        TeamService teamService = new TeamService(teamRepo);
        HackathonService hackathonService = new HackathonService(hackRepo, teamService, staffService); 
        InviteService inviteService = new InviteService(inviteRepo, teamService, userRepo);
        SubmissionService submissionService = new SubmissionService(submissionRepo, hackRepo, teamRepo); 
        ViolationService violationService = new ViolationService(violationRepo, staffService); 
        
        violationService.setHackathonRepo(hackRepo);
        violationService.setTeamService(teamService);

        InviteController inviteController = new InviteController(inviteService);
        SubmissionController submissionController = new SubmissionController(submissionService);
        ViolationController violationController = new ViolationController(violationService);

        //Creazione Utenti di Test
        User leader = new User("Mario", "Rossi", "mario@mail.it", "123", "U1");
        User u2 = new User("Luca", "Bianchi", "luca@mail.it", "123", "U2");
        User u3 = new User("Anna", "Verdi", "anna@mail.it", "123", "U3");
        User mentor = new User("Paolo", "Mentor", "mentor@mail.it", "123", "U7");
        User judge = new User("Giulia", "Judge", "judge@mail.it", "123", "U8");
        User uOrganizer = new User("Stefano", "Organizer", "org@mail.it", "123", "U10"); 

        userRepo.save(leader);
        userRepo.save(u2);
        userRepo.save(u3);
        userRepo.save(mentor);
        userRepo.save(judge);
        userRepo.save(uOrganizer);

        // Creazione record staff per non far fallire i controlli dei casi d'uso
        Organizer organizer = new Organizer("ORG1", uOrganizer, "H1");
        Mentor mentorStaff = new Mentor("MNT1", mentor, "H1");
        staffRepository.save(organizer);
        staffRepository.save(mentorStaff);

        // Test Iterazione 1
        Team team = teamService.createTeam("T1", "TeamRocket", leader);

        System.out.println("Team creato: " + team.getName());
        System.out.println("Leader: " + team.getLeader().getUser().getName());

        team.addMember(new TeamMember("TM2", u2, TeamMember.Role.MEMBER));
        team.addMember(new TeamMember("TM3", u3, TeamMember.Role.MEMBER));

        System.out.println("Membri team: " + team.getMembers().size());

        // Test State Pattern su Hackathon 
        Hackathon hackathon = hackathonService.createHackathon("H1", "Hackathon Test", "a", "ORG1");

        System.out.println("Hackathon creato: " + hackathon.getName());
        System.out.println("Stato iniziale: " + hackathon.getState().getName());

        hackathon.iscriviTeam(team);
        System.out.println("Team iscritti: " + hackathon.getTeams().size());

        User leader2 = new User("Giulia", "Neri", "giulia@mail.it", "123", "U4");
        userRepo.save(leader2);

        Team team2 = teamService.createTeam("T2", "TeamBlue", leader2);
        team2.addMember(new TeamMember("TM4", new User("Marco", "Rossi", "m", "p", "U5"), TeamMember.Role.MEMBER));
        team2.addMember(new TeamMember("TM5", new User("Sara", "Verdi", "s", "p", "U6"), TeamMember.Role.MEMBER));

        hackathon.iscriviTeam(team2);
        System.out.println("Team iscritti dopo secondo team: " + hackathon.getTeams().size());

        // Avanzamento di stato: passa a IN_CORSO
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
            User u9 = new User("Elena", "Test", "elena@mail.it", "123", "U9");
            userRepo.save(u9);

            inviteController.sendInvite("U1", "T1", "U9");
            Invite lastInvite = inviteRepo.findAll().get(inviteRepo.findAll().size() - 1);
            inviteController.acceptInvite(lastInvite.getId());

            team = teamService.getById("T1"); 
            System.out.println("Membri TeamRocket dopo accettazione U9: " + team.getMembers().size());
        } catch (Exception e) {
            System.out.println("Errore ciclo invito: " + e.getMessage());
        }

        // UC: Sottomissione Progetto (Send / Edit)
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
            violationController.chooseNoAction(violationId);
            
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
    }
}