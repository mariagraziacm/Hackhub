package it.unicam.hackhub.model;

public abstract class MembroStaff {
    private String ruolo;
    private String userId;

    public MembroStaff(String ruolo, String userId) {
        this.ruolo = ruolo;
        this.userId = userId;
    }

    public String getRuolo() {
        return ruolo;
    }

    public String getUserId() {
        return userId;
    }
}