package Principale.Parcheggio.Models;

import jakarta.persistence.*;


@Entity
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int posti_totali_sosta;
    private int posti_totali_ricarica;

    public ParkingSpot() {}

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosti_totali_ricarica() {
        return posti_totali_ricarica;
    }

    public void setPosti_totali_ricarica(int posti_totali) {
        this.posti_totali_ricarica = posti_totali;
    }

    public int getPosti_totali_sosta() {
        return posti_totali_sosta;
    }

    public void setPosti_totali_sosta(int posti_totali) {
        this.posti_totali_sosta = posti_totali;
    }
}


