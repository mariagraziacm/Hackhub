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
        System.out.println("TEST HACKHUB");

        TeamRepository teamRepository = new TeamRepository();
        UsersRepository usersRepository = new UsersRepository();
        PartecipationController PartecipationController = new PartecipationController(teamRepository, usersRepository);

        User leader = new User("Mario", "Rossi", "mario@test.it", "pwd", "USR-01");
        User richiedente = new User("Anna", "Verdi", "anna@test.it", "pwd", "USR-02");
        usersRepository.save(leader);
        usersRepository.save(richiedente);

        Team teamAlpha = new Team("T-ALPHA", "Prova");
        teamAlpha.getMembers().add(new TeamMember("TM-01", "T-ALPHA", null, leader));
        teamRepository.save(teamAlpha);

        System.out.println("\nTest Richiesta Valida");
        PartecipationController.sendRequest("T-ALPHA", "USR-02");

        int quanteRichieste = PartecipationController.getRequests().size();
        System.out.println("Numero di oggetti salvati nella lista del Controller: " + quanteRichieste);

        if (quanteRichieste > 0) {
            PartecipationRequest richiestaInMemoria = PartecipationController.getRequests().get(0);
            System.out.println("Chi richiede: " + richiedente.getName() + " " + richiedente.getSurname() + " (ID: " + richiedente.getid() + ")");
            System.out.println("Team richiesto: " + teamAlpha.getName() + " (ID: " + teamAlpha.getId() + ")");
        }

        System.out.println("Test Scenario Alternativo ");
        for (int i = 0; i < 5; i++) {
            User utenteMock = new User("Membro" + i, "Test", "mock@test.it", "pwd", "USR-MOCK-" + i);
            usersRepository.save(utenteMock);

            TeamMember membroMock = new TeamMember("TM-MOCK-" + i, "T-ALPHA", null, utenteMock);
            teamAlpha.getMembers().add(membroMock);
        }
        PartecipationController.sendRequest("T-ALPHA", "USR-02");
    }
}