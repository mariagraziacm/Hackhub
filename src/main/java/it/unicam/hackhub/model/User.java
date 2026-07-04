package it.unicam.hackhub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users") // Evita conflitti con parole riservate SQL come "user"
public class User {

    @Id // Definisce la chiave primaria sul database
    private String id; 

    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private String paymentInfo;

    // Spring Boot / JPA richiede obbligatoriamente un costruttore vuoto (senza argomenti)
    public User() {
    }

    // Costruttore completo per creare l'utente all'interno del tuo codice
    public User(String id, String name, String surname, String username, String email, String password, String paymentInfo) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.paymentInfo = paymentInfo;
    }

    // --- GETTER ---
    public String getId() { return id; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getPassword() { return password; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPaymentInfo() { return paymentInfo; }

    // --- SETTER (Utili per gli aggiornamenti dei dati con JPA) ---
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setPaymentInfo(String paymentInfo) { this.paymentInfo = paymentInfo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    // La tua logica di business originale rimane intatta
    public boolean hasPaymentInfo() {
        return paymentInfo != null && !paymentInfo.isBlank();
    }
}