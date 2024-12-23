package Principale.Parcheggio.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "parking_spots")
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = 1;

    private int posti_totali = 2;

    public ParkingSpot() {}

    // Getters and Setters
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

