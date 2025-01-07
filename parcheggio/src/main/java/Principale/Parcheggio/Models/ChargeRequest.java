package Principale.Parcheggio.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.hibernate.engine.spi.SessionDelegatorBaseImpl;

import java.sql.Time;
import java.time.LocalDate;

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
    private Integer Percentuale_iniziale;
    private Integer Percentuale_richiesta;
    private Time durata;

    private Time Ora;

    private Time OraFine;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Relazione con l'entità User

    @Column(name = "pagare", nullable = false, columnDefinition = "FLOAT DEFAULT 0")
    private double Pagare;

    private String Targa;

    private Boolean ricarica;


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

    public Integer getpercentuale_iniziale() {
        return Percentuale_iniziale;
    }

    public void setPercentuale_iniziale(Integer percentuale) {
        this.Percentuale_iniziale = percentuale;
    }

    public Integer getPercentuale_richiesta() {
        return Percentuale_richiesta;
    }

    public void setPercentuale_richiesta(Integer percentuale) {
        this.Percentuale_richiesta = percentuale;
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

    public Time getOraFine() {
        return OraFine;
    }

    public void setOraFine(Time oraFine) {
        OraFine = oraFine;
    }

    public String getTarga() {
        return Targa;
    }

    public void setTarga(String targa) {
        Targa = targa;
    }

    public Boolean getRicarica() {
        return ricarica;
    }

    public void setRicarica(Boolean ricarica) {
        this.ricarica = ricarica;
    }


}


