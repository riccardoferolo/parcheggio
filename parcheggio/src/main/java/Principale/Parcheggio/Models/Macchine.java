package Principale.Parcheggio.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "macchine")
public class Macchine {

    @Id
    @Column(name = "targa", unique = true, nullable = false)
    private String targa; // Chiave primaria univoca

    private double kwBatteria;
    private String modello;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Relazione con User (Molte Macchine -> Un Utente)

    // Costruttore vuoto
    public Macchine() {
    }

    // Costruttore completo
    public Macchine(String targa, double kwBatteria, String modello, User user) {
        this.targa = targa;
        this.kwBatteria = kwBatteria;
        this.modello = modello;
        this.user = user;
    }

    // Getters e Setters
    public String getTarga() {
        return targa;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }

    public double getKwBatteria() {
        return kwBatteria;
    }

    public void setKwBatteria(double kwBatteria) {
        this.kwBatteria = kwBatteria;
    }

    public String getModello() {
        return modello;
    }

    public void setModello(String modello) {
        this.modello = modello;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Macchina{" +
                "targa='" + targa + '\'' +
                ", kwBatteria=" + kwBatteria +
                ", modello='" + modello + '\'' +
                ", user=" + user.getUsername() +
                '}';
    }
}

