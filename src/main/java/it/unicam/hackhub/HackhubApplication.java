/*package it.unicam.hackhub;

import it.unicam.hackhub.model.*;
import it.unicam.hackhub.repository.*;
import it.unicam.hackhub.service.*;
import it.unicam.hackhub.state.InValutazioneState;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import it.unicam.hackhub.config.SecurityConfig;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SecurityConfig.class)
public class HackhubApplication {

    public static void main(String[] args) {
        SpringApplication.run(HackhubApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(
            UserRepository userRepo,
            TeamRepository teamRepo,
            HackathonRepository hackRepo,
            InviteRepository inviteRepo,
            SubmissionRepository submissionRepo,
            ViolationRepository violationRepo,
            StaffRepository staffRepository,
            CallRepository callRepo,
            TeamService teamService,
            HackathonService hackathonService,
            InviteService inviteService,
            SubmissionService submissionService,
            ViolationService violationService,
            SupportRequestRepository supportRequestRepo,
            StaffService staffService
    ) {
        return args -> {
            System.out.println("\n🚀 Avvio inizializzazione Dati di Test nel Database H2...");

            // --- INSERIMENTO UTENTI ---
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

            // --- ITERAZIONE 1 ---
            Team team = teamService.createTeam("T1", "TeamRocket", leader);
            System.out.println("Team creato: " + team.getName());

            team.addMember(new TeamMember("TM2", u2, TeamMember.Role.MEMBER));
            System.out.println("Membri team: " + team.getMembers().size());

            Hackathon hackathon = hackathonService.createHackathon("H1", "Hackathon Test", "a", "ORG1");
            System.out.println("Hackathon creato: " + hackathon.getName());

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

            hackathon.nextState();
            hackRepo.save(hackathon);
            System.out.println("Nuovo stato: " + hackathon.getState().getName());

            try {
                Team team3 = teamService.createTeam("T3", "TeamBlocked", leader);
                hackathon.iscriviTeam(team3);
            } catch (Exception e) {
                System.out.println("Errore atteso (state block): " + e.getMessage());
            }

            System.out.println("\n--- ITERAZIONE 2 (Popolamento tramite Service) ---");

            // Chiamiamo direttamente i Service invece dei Controller per evitare conflitti con i DTO delle richieste HTTP
            try {
                inviteService.inviteMentor("H1", "U7");
                inviteService.inviteJudge("H1", "U8");
                System.out.println("Inviti salvati nel repository: " + inviteRepo.findAll().size());
            } catch (Exception e) {
                System.out.println("Errore inviti: " + e.getMessage());
            }

            try {
                User u9 = new User("Elena", "Test", "elena_user", "elena@mail.it", "123", "U9", null);
                userRepo.save(u9);

                Invite invite = inviteService.sendInvite("U1", "T1", "U9");
                inviteService.acceptInvite(invite.getId());

                team = teamService.getById("T1");
                System.out.println("Membri TeamRocket dopo accettazione U9: " + team.getMembers().size());
            } catch (Exception e) {
                System.out.println("Errore ciclo invito: " + e.getMessage());
            }

            try {
                Submission sub = submissionService.sendSubmission("H1", "T1", "Project Rocket", "Descrizione progetto");
                submissionService.editSubmission(sub.getId(), "Project Rocket v2", "Descrizione aggiornata");

                System.out.println("Titolo submission aggiornato in repository: " + submissionRepo.findAll().get(0).getTitle());
            } catch (Exception e) {
                System.out.println("Errore submission: " + e.getMessage());
            }

            try {
                Violation violation = violationService.createViolation("H1", "T1", "U2", "MNT1", "Comportamento scorretto");
                violationService.resolveViolation(violation.getId(), Violation.ViolationStatus.PENDING);

                System.out.println("Violazione registrata per il team: " + violationRepo.findAll().size());
            } catch (Exception e) {
                System.out.println("Errore violazione: " + e.getMessage());
            }

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
                System.out.println("Errore use cases team: " + e.getMessage());
            }

            System.out.println("\n--- UC: SUPPORT REQUEST ---");
            try {
                SupportRequestService supportRequestService = new SupportRequestService(
                        supportRequestRepo, teamService, staffService, hackRepo, callRepo
                );
                supportRequestService.sendRequest("T1", "U2", "MNT1", "H1", "Ho bisogno di aiuto sul progetto");
                System.out.println("Support requests create: 1");
            } catch (Exception e) {
                System.out.println("Errore support request: " + e.getMessage());
            }

            System.out.println("\n--- UC: PROCLAMA VINCITORE ---");
            try {
                if (!(hackathon.getState() instanceof InValutazioneState)) {
                    hackathon.nextState();
                }
                hackathon.proclamaVincitore(team2);
                System.out.println("🏆 Vincitore: " + hackathon.getWinner().getName());
            } catch (Exception e) {
                System.out.println("Errore proclamazione vincitore: " + e.getMessage());
            }

            System.out.println("\n--- ITERAZIONE TRE ---");
            System.out.println("\n--- UC: VALUTA SOTTOMISSIONE ---");
            try {
                hackathon.valutaSottomissione(team, submissionRepo.findAll().get(0), 9, "Ottimo lavoro");
                System.out.println("Valutazione completata per team: " + team.getName());
            } catch (Exception e) {
                System.out.println("Errore valutazione: " + e.getMessage());
            }

            System.out.println("\n✅ Inizializzazione completata. Il database H2 è pronto e tutti gli errori sono risolti!");
        };
    }
}*/
package it.unicam.hackhub;

import it.unicam.hackhub.model.*;
import it.unicam.hackhub.repository.*;
import it.unicam.hackhub.service.*;
import it.unicam.hackhub.state.InValutazioneState;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import it.unicam.hackhub.config.SecurityConfig;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SecurityConfig.class)
public class HackhubApplication {

    public static void main(String[] args) {
        SpringApplication.run(HackhubApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(
            UserRepository userRepo,
            TeamRepository teamRepo,
            HackathonRepository hackRepo,
            InviteRepository inviteRepo,
            SubmissionRepository submissionRepo,
            ViolationRepository violationRepo,
            StaffRepository staffRepository,
            CallRepository callRepo,
            TeamService teamService,
            HackathonService hackathonService,
            InviteService inviteService,
            SubmissionService submissionService,
            ViolationService violationService,
            SupportRequestRepository supportRequestRepo,
            StaffService staffService
    ) {
        return args -> {
            System.out.println("\n🚀 Avvio inizializzazione Dati di Test nel Database MySQL...");

            // --- INSERIMENTO UTENTI ---
            // Costruttore: User(id, name, surname, username, email, password, paymentInfo)
            User leader = new User("U1", "Mario", "Rossi", "mario_user", "mario@mail.it", "123", null);
            User u2 = new User("U2", "Luca", "Bianchi", "luca_user", "luca@mail.it", "123", null);
            User u3 = new User("U3", "Anna", "Verdi", "anna_user", "anna@mail.it", "123", null);
            User mentor = new User("U7", "Paolo", "Mentor", "mentor_user", "mentor@mail.it", "123", null);
            User judge = new User("U8", "Giulia", "Judge", "judge_user", "judge@mail.it", "123", null);
            User uOrganizer = new User("U10", "Stefano", "Organizer", "org_user", "org@mail.it", "123", null);

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

            // =================================================================
            // CREAZIONE DEI 3 HACKATHON DI TEST SOLLECITATI
            // =================================================================

            // --- HACKATHON 1 (Sarà quello che si CONCLUDERÀ alla fine del flusso) ---
            Hackathon hackathon = hackathonService.createHackathon("H1", "Hackathon Test Concluso", "Specifiche H1", "ORG1");
            System.out.println("Hackathon 1 creato: " + hackathon.getName());

            // --- HACKATHON 2 (Rimarrà in FASE DI ISCRIZIONE per i test) ---
            Hackathon h2 = hackathonService.createHackathon("H2", "AI Global Challenge", "Specifiche H2 - Iscrizioni Aperte", "ORG1");
            System.out.println("Hackathon 2 creato (In Iscrizione): " + h2.getName());

// AGGIUNTA: Creiamo un team di test e lo iscriviamo a H2
            User uTester = new User("U_TEST", "User", "test_user", "test@mail.it", "123", "U_TEST", null);
            userRepo.save(uTester);
            Team teamPerH2 = teamService.createTeam("T_H2", "AI_Explorers", uTester);

// Iscrizione tramite il metodo del tuo modello (che usa lo State Pattern)
            h2.addTeamToHackathon(teamPerH2);
            hackRepo.save(h2); // IMPORTANTE: Questo salva il collegamento nella tabella di join!

            System.out.println("Hackathon 2 creato e popolato con AI_Explorers: " + h2.getName());

            // --- HACKATHON 3 (Sarà impostato in FASE IN CORSO per i test) ---
            Hackathon h3 = hackathonService.createHackathon("H3", "Cybersecurity Days", "Specifiche H3 - Gara in corso", "ORG1");
            User leaderExtra = new User("U11", "Neri", "rob_user", "rob@mail.it", "123", "U11", null);
            userRepo.save(leaderExtra);
            Team teamForH3 = teamService.createTeam("T4", "TeamShadow", leaderExtra);
            h3.addTeamToHackathon(teamForH3);
            h3.nextState(); // Avanza a IN CORSO
            hackRepo.save(h3); // Salva lo stato IN CORSO nel DB
            System.out.println("Hackathon 3 creato e impostato su IN CORSO: " + h3.getName());

            // =================================================================
            // SEGUIMENTO DEL FLUSSO DI BUSINESS SU H1 (ITERAZIONE 1 & 2)
            // =================================================================

            Team team = teamService.createTeam("T1", "TeamRocket", leader);
            team.addMember(new TeamMember("TM2", u2, TeamMember.Role.MEMBER));
            hackathon.addTeamToHackathon(team);

            User leader2 = new User("U4", "Neri", "giulia_user", "giulia@mail.it", "123", "U4", null);
            userRepo.save(leader2);

            Team team2 = teamService.createTeam("T2", "TeamBlue", leader2);
            User marco = new User("U5", "Rossi", "marco_user", "m", "p", "U5", null);
            User sara = new User("U6", "Verdi", "sara_user", "s", "p", "U6", null);
            userRepo.save(marco);
            userRepo.save(sara);

            team2.addMember(new TeamMember("TM4", marco, TeamMember.Role.MEMBER));
            team2.addMember(new TeamMember("TM5", sara, TeamMember.Role.MEMBER));
            hackathon.addTeamToHackathon(team2);

            // Spostiamo H1 in CORSO per simulare i test successivi
            hackathon.nextState();
            hackRepo.save(hackathon);
            System.out.println("Hackathon 1 spostato in: " + hackathon.getState().getName());

            try {
                Team team3 = teamService.createTeam("T3", "TeamBlocked", leader);
                hackathon.addTeamToHackathon(team3);
            } catch (Exception e) {
                System.out.println("Errore atteso (state block iscrizione su H1): " + e.getMessage());
            }

            System.out.println("\n--- ITERAZIONE 2 (Popolamento tramite Service) ---");

            try {
                inviteService.inviteMentor("H1", "U7");
                inviteService.inviteJudge("H1", "U8");
            } catch (Exception e) {
                System.out.println("Errore inviti: " + e.getMessage());
            }

            try {
                User u9 = new User("U9", "Test", "elena_user", "elena@mail.it", "123", "U9", null);
                userRepo.save(u9);
                Invite invite = inviteService.sendInvite("U1", "T1", "U9");
                inviteService.acceptInvite(invite.getId());
                team = teamService.getById("T1");
            } catch (Exception e) {
                System.out.println("Errore ciclo invito: " + e.getMessage());
            }

            try {
                Submission sub = submissionService.sendSubmission("H1", "T1", "Project Rocket", "Descrizione progetto");
                submissionService.editSubmission(sub.getId(), "Project Rocket v2", "Descrizione aggiornata");
            } catch (Exception e) {
                System.out.println("Errore submission: " + e.getMessage());
            }

            try {
                Violation violation = violationService.createViolation("H1", "T1", "U2", "MNT1", "Comportamento scorretto");
                violationService.resolveViolation(violation.getId(), Violation.ViolationStatus.PENDING);
            } catch (Exception e) {
                System.out.println("Errore violazione: " + e.getMessage());
            }

            try {
                teamService.changeLeader("U1", "T1", "U2");
                teamService.removeMember("U2", "T1", "U3");
                teamService.leaveTeam("T1", "U9");
            } catch (Exception e) {
                System.out.println("Errore use cases team: " + e.getMessage());
            }

            System.out.println("\n--- UC: SUPPORT REQUEST ---");
            try {
                SupportRequestService supportRequestService = new SupportRequestService(
                        supportRequestRepo, teamService, staffService, hackRepo, callRepo
                );
                supportRequestService.sendRequest("T1", "U2", "MNT1", "H1", "Ho bisogno di aiuto sul progetto");
            } catch (Exception e) {
                System.out.println("Errore support request: " + e.getMessage());
            }

            System.out.println("\n--- UC: PROCLAMA VINCITORE SU H1 ---");
            try {
                if (!(hackathon.getState() instanceof InValutazioneState)) {
                    hackathon.nextState(); // Va in InValutazioneState
                }
                hackathon.proclaimWinner(team2); // Diventa CONCLUSO nello State Pattern
                hackRepo.save(hackathon); // FISSATO: Salviamo lo stato Concluso nel DB MySQL!
                System.out.println("🏆 Vincitore proclamato per H1! Stato attuale del DB: " + hackathon.getState().getName());
            } catch (Exception e) {
                System.out.println("Errore proclamazione vincitore: " + e.getMessage());
            }

            System.out.println("\n--- ITERAZIONE TRE: VALUTA SOTTOMISSIONE ---");
            try {
                hackathon.rateSubmission(team, submissionRepo.findAll().get(0), 9, "Ottimo lavoro");
                hackRepo.save(hackathon);
                System.out.println("Valutazione completata per team: " + team.getName());
            } catch (Exception e) {
                System.out.println("Errore valutazione: " + e.getMessage());
            }

            System.out.println("\n✅ Inizializzazione completata! Database MySQL pronto con i 3 Hackathon nelle 3 fasi corrette.");
        };
    }
}