package it.unicam.hackhub;

import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.service.HackathonService;
import it.unicam.hackhub.repository.HackathonRepository;
import it.unicam.hackhub.repository.TeamRepository;
import it.unicam.hackhub.service.TeamService;
import it.unicam.hackhub.repository.UserRepository;
import it.unicam.hackhub.repository.UserRepository;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.model.TeamMember;
import it.unicam.hackhub.model.Role;

public class Main {
    public static void main(String[] args) {
        TeamRepository teamRepo = new TeamRepository();
        UserRepository userRepo = new UserRepository();
        HackathonRepository hackRepo = new HackathonRepository();

        TeamService teamService = new TeamService(teamRepo);
        HackathonService hackathonService = new HackathonService(hackRepo, teamService);


        User leader = new User("Mario", "Rossi", "mario@mail.it", "123", "U1");
        User u2 = new User("Luca", "Bianchi", "luca@mail.it", "123", "U2");
        User u3 = new User("Anna", "Verdi", "anna@mail.it", "123", "U3");

        userRepo.save(leader);
        userRepo.save(u2);
        userRepo.save(u3);


        Team team = teamService.createTeam("T1", "TeamRocket", leader);

        System.out.println("Team creato: " + team.getName());
        System.out.println("Leader: " + team.getLeader().getUser().getName());

        team.addMember(new TeamMember("TM2", u2, Role.MEMBER));
        team.addMember(new TeamMember("TM3", u3, Role.MEMBER));

        System.out.println("Membri team: " + team.getMembers().size());


        Hackathon hackathon = hackathonService.createHackathon(
                "H1",
                "Hackathon Test",
                "a"
        );

        System.out.println("Hackathon creato: " + hackathon.getName());
        System.out.println("Stato iniziale: " + hackathon.getState().getName());

        hackathon.iscriviTeam(team);

        System.out.println("Team iscritti: " + hackathon.getTeams().size());

        User leader2 = new User("Giulia", "Neri", "giulia@mail.it", "123", "U4");
        userRepo.save(leader2);

        Team team2 = teamService.createTeam("T2", "TeamBlue", leader2);

        team2.addMember(new TeamMember("TM4", new User("Marco","Rossi","m","p","U5"), Role.MEMBER));
        team2.addMember(new TeamMember("TM5", new User("Sara","Verdi","s","p","U6"), Role.MEMBER));

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
    }
}