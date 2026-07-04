package it.unicam.hackhub.external.price;

import it.unicam.hackhub.model.Hackathon;
import it.unicam.hackhub.model.Team;
import it.unicam.hackhub.model.TeamMember;
import it.unicam.hackhub.repository.HackathonRepository;
import it.unicam.hackhub.state.ConclusoState;

public class PrizeService {
    private final HackathonRepository hackathonRepo;
    private final PaymentService paymentService;

    public PrizeService(HackathonRepository hackathonRepo,
                        PaymentService paymentService) {
        this.hackathonRepo = hackathonRepo;
        this.paymentService = paymentService;
    }

    public void erogaPremio(String hackathonId, String organizerId) {

        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalStateException("Hackathon non trovato"));

        // controllo organizzatore
        if (!hackathon.getOrganizer().getId().equals(organizerId)) {
            throw new IllegalStateException("Solo l'organizzatore può erogare il premio");
        }

        // hackathon concluso
        if (!(hackathon.getState() instanceof ConclusoState)) {
            throw new IllegalStateException("L'hackathon non è concluso");
        }

        // vincitore presente
        Team winner = hackathon.getWinner();

        if (winner == null) {
            throw new IllegalStateException("Nessun team vincitore proclamato");
        }

        // pagamento a tutti i membri
        for (TeamMember member : winner.getMembers()) {
            PaymentResult result = paymentService.erogaPremio(member.getUser());

            if (!result.isSuccess()) {
                System.out.println(
                        "⚠ Pagamento fallito per "
                                + member.getUser().getName()
                                + " -> "
                                + result.getMessage()
                );
            } else {
                System.out.println(
                        "✔ Pagamento effettuato per "
                                + member.getUser().getName()
                );
            }
        }
    }
}
