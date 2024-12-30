package Principale.Parcheggio.Models;

import jakarta.persistence.*;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String password;
    private String email;
    private double saldo;

    private Long Carta_di_credito;

    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    // Getter corretto per saldo
    public double getSaldo() {
        return saldo;
    }

    // Setter corretto per saldo
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Long getCarta_di_credito() {
        return Carta_di_credito;
    }

    public void setCarta_di_credito(Long carta_di_credito) {
        Carta_di_credito = carta_di_credito;
    }

    public User(){

    }

}
