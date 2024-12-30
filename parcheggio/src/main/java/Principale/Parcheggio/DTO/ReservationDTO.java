package Principale.Parcheggio.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Time;
import java.time.LocalDate;

public class ReservationDTO {
    private Long id;

    @JsonProperty("userId")
    private int userId;

    @JsonProperty("chargeRequestId")
    private Long chargeRequestId;

    @JsonProperty("paymentId")
    private Long paymentId;

    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonProperty("durata")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private Time durata;

    @JsonProperty("time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private Time time;

    @JsonProperty("Targa")
    private String targa;

    public ReservationDTO() {
        // Costruttore vuoto richiesto da Jackson
    }

    // Costruttore
    public ReservationDTO(Long id, int userId, Long chargeRequestId, Long paymentId,
                          LocalDate date, Time time, Time durata, String targa) {
        this.id = id;
        this.userId = userId;
        this.chargeRequestId = chargeRequestId;
        this.paymentId = paymentId;
        this.date = date;
        this.time = time;
        this.durata = durata;
        this.targa = targa;
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

    public LocalDate getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Time getDurata() {
        return durata;
    }

    public String getTarga() {
        return targa;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }
}
