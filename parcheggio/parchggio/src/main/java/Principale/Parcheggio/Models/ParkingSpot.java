package Principale.Parcheggio.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "parking_spots")
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_occupied", nullable = false, columnDefinition = "boolean default false")
    private boolean isOccupied = false;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAvailable() {
        this.isOccupied = false;
    }

    public void setOccupied() {
        this.isOccupied = true;
    }
    public boolean isOccupied() {
        return this.isOccupied;
    }
}

