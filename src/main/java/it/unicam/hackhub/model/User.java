package it.unicam.hackhub.model;

public class User {
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private final String id;

    public User(String name, String surname, String username, String email, String password, String id) {
        this.name = name;
        this.surname = surname;
        this.username=username;
        this.email = email;
        this.password = password;
        this.id = id;
    }
    public String getId {return id;}
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getPassword {return password; }
    public String getUsername(){return username;}
    public String getEmail() {
        return email;
    }

}