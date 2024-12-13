package Principale.Parcheggio.Models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Aggiunta di un campo ID per l'entità

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Relazione con l'entità User

    @ManyToOne
    @JoinColumn(name = "charge_request_id", nullable = false)
    private ChargeRequest chargeRequest; // Relazione con l'entità ChargeRequest

    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)  // Cambiato da false a true
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "parking_spot_id", nullable = false)
    private ParkingSpot parkingSpot; // Relazione con l'entità ParkingSpot

    // Getters e Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ChargeRequest getChargeRequest() {
        return chargeRequest;
    }

    public void setChargeRequest(ChargeRequest chargeRequest) {
        this.chargeRequest = chargeRequest;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

}

