package it.unicam.hackhub.external.prize;

import it.unicam.hackhub.model.User;

public class ExternalPaymentService implements PaymentService {
    @Override
    public PaymentResult erogaPremio(User user) {

        
        return new PaymentResult(user, true, "Pagamento simulato eseguito con successo");
    }
}
