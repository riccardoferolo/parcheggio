package Principale.Parcheggio.Models;

import jakarta.persistence.*;


@Entity
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int posti_totali;

    public ParkingSpot() {}

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosti_totali() {
        return posti_totali;
    }

    public void setPosti_totali(int posti_totali) {
        this.posti_totali = posti_totali;
    }
}


