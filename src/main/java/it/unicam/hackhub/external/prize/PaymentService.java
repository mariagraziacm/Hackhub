package it.unicam.hackhub.external.prize;

import it.unicam.hackhub.model.User;

public interface PaymentService {
        PaymentResult erogaPremio(User user);
}
