package it.unicam.hackhub.controller;

import it.unicam.hackhub.model.PartecipationRequest;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.User;
import it.unicam.hackhub.repository.TeamRepository;
import it.unicam.hackhub.repository.UsersRepository;
import it.unicam.hackhub.service.PartecipationRequestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PartecipationController {
    private final PartecipationRequestService requestService;

    public PartecipationController(PartecipationRequestService requestService) {
        this.requestService = requestService;
    }

    public void sendRequest(String idTeam, String idUser) {
        try {
            PartecipationRequest req = requestService.sendRequest(idTeam, idUser);
            System.out.println("SYSTEM: Richiesta di partecipazione (" + req.getId() + ") inviata con successo al Leader del team '" + req.getTeam().getName() + "'.");
        } catch (IllegalStateException e) {
            System.out.println("SYSTEM [ERRORE]: " + e.getMessage());
        }
    }
}