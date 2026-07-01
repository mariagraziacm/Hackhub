package it.unicam.hackhub.model;

public abstract class MembroStaff {
    private String role;
    private String userId;

    public MembroStaff(String role, String userId) {
        this.role = role;
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }
    public String getUserId() {
        return userId;
    }
}