package it.unicam.hackhub.external.price;

public class PrizeController {
    private final PrizeService prizeService;

    public PrizeController(PrizeService prizeService) {
        this.prizeService = prizeService;
    }

    public void erogaPremio(String hackathonId, String organizerId) {

        try {
            prizeService.erogaPremio(hackathonId, organizerId);
            System.out.println("SYSTEM: Premio erogato correttamente.");
        } catch (Exception e) {
            System.out.println("SYSTEM [ERRORE]: " + e.getMessage());
        }

    }
}
