package Principale.Parcheggio.DTO;

import org.springframework.cglib.core.Local;

import java.sql.Time;
import java.time.LocalDate;

public class ReservationDTO {
    private Long id;
    private int userId;
    private Long chargeRequestId;
    private Long paymentId;
    private int parkingSpotId;
    private LocalDate date;
    private Time time;
    private Time durata;


    // Costruttore
    public ReservationDTO(Long id, int userId, Long chargeRequestId, Long paymentId, int parkingSpotId, LocalDate date, Time time, Time durata) {
        this.id = id;
        this.userId = userId;
        this.chargeRequestId = chargeRequestId;
        this.paymentId = paymentId;
        this.parkingSpotId = parkingSpotId;
        this.date = date;
        this.time = time;
        this.durata = durata;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public Long getChargeRequestId() {
        return chargeRequestId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public int getParkingSpotId() {
        return parkingSpotId;
    }
    public LocalDate getDate() {
        return date;
    }
    public Time getTime() {
        return time;
    }

    public Time getDurata() {
        return durata;
    }

}

