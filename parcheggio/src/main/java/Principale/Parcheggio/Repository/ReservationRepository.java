package Principale.Parcheggio.Repository;

import Principale.Parcheggio.Models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByChargeRequestId(long chargeRequestId); // Metodo per trovare una prenotazione basata sull'ID della richiesta di carica

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE Reservations ALTER COLUMN id RESTART WITH 1;\n", nativeQuery = true)
    void resetAutoIncrement();


    @Query("SELECT r FROM Reservation r WHERE r.ricarica = true")
    List<Reservation> findAllByRicaricaTrue();

    @Query("SELECT r.chargeRequest.Ora as ora, r.chargeRequest.Giorno as giorno, " +
            "SUM(CASE WHEN r.ricarica = true THEN 1 ELSE 0 END) as ricariche, " +
            "SUM(CASE WHEN r.ricarica IS NULL OR r.ricarica = false THEN 1 ELSE 0 END) as soste " +
            "FROM Reservation r " +
            "WHERE CURRENT_TIMESTAMP BETWEEN r.chargeRequest.Ora AND r.chargeRequest.OraFine " +
            "GROUP BY r.chargeRequest.Giorno, r.chargeRequest.Ora " +
            "ORDER BY r.chargeRequest.Giorno, r.chargeRequest.Ora")
    List<Object[]> Occupazione();
    }


