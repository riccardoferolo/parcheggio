package Principale.Parcheggio.Models;

import jakarta.persistence.*;
import Principale.Parcheggio.Models.ChargeRequest;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "charge_request_id", nullable = false)
    private ChargeRequest chargeRequest;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private boolean isPaid;
    private double totalAmount;

    // Costruttore di default
    public Payment() {}

    // Costruttore con parametri
    public Payment(ChargeRequest chargeRequest, User user) {
        this.chargeRequest = chargeRequest;
        this.user = user;
        this.isPaid = false;
        this.totalAmount = chargeRequest.getPagare();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChargeRequest getChargeRequest() {
        return chargeRequest;
    }

    public void setChargeRequest(ChargeRequest chargeRequest) {
        this.chargeRequest = chargeRequest;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    // hashCode, equals e toString (se necessario, puoi aggiungerli)
}


