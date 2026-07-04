package it.unicam.hackhub.external.price;

import it.unicam.hackhub.model.User;

public class PaymentResult {
    private final User user;
    private final boolean success;
    private final String message;

    public PaymentResult(User user, boolean success, String message) {
        this.user = user;
        this.success = success;
        this.message = message;
    }

    public User getUser() { return user; }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}
