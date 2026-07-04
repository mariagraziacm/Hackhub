package it.unicam.hackhub.model;

public class User {
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private final String id;
    private String paymentInfo;

    public User(String name, String surname, String username, String email, String password, String id,String paymentInfo) {
        this.name = name;
        this.surname = surname;
        this.username=username;
        this.email = email;
        this.password = password;
        this.id = id;
        this.paymentInfo=paymentInfo;
    }
    public String getId() {return id; }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getPassword() {return password; }
    public String getUsername(){return username;}
    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public boolean hasPaymentInfo() {
        return paymentInfo != null && !paymentInfo.isBlank();
    }

}