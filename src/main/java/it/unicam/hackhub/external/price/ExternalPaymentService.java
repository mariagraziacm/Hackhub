package it.unicam.hackhub.external.price;

import it.unicam.hackhub.model.User;

public class ExternalPaymentService implements PaymentService {
    @Override
    public void erogaPremio(User user) {

        // qui un domani verrà chiamato il gateway esterno
        // Stripe
        // PayPal
        // Bonifico
        // ecc.

    }
}
