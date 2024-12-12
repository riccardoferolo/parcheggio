package Principale.Parcheggio.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class ChargeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Aggiunta di un campo ID per l'entità
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate Giorno;
    private Integer Percentuale;
    private Time durata;

    private Time Ora;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Relazione con l'entità User

    @Column(name = "pagare", nullable = false, columnDefinition = "FLOAT DEFAULT 0")
    private double Pagare;



    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Time getdurata() {
        return durata;
    }

    public void setdurata(Time durata) {
        this.durata = durata;
    }

    public Integer getpercentuale() {
        return Percentuale;
    }

    public void setpercentuale(Integer percentuale) {
        this.Percentuale = percentuale;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getGiorno() {
        return Giorno;
    }

    public void setGiorno(LocalDate giorno) {
        this.Giorno = giorno;
    }

    public Time getOra() {
        return Ora;
    }

    public void setOra(Time ora) {
        this.Ora = ora;
    }

    public double getPagare() {
        return Pagare;
    }

    public void setPagare(double pagare) {
        this.Pagare = pagare;
    }


}


