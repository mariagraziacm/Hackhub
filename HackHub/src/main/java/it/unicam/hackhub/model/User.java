package it.unicam.hackhub.model;

public class User {
    private String name;
    private String surname;
    private String email;
    private String password;
    private final String id;

    public User(String name, String surname, String email, String password, String id) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getEmail() {
        return email;
    }
    public String getId(){return id;}
}