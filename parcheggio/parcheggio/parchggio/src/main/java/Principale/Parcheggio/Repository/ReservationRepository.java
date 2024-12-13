package Principale.Parcheggio.Repository;

import Principale.Parcheggio.Models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByChargeRequestId(long chargeRequestId); // Metodo per trovare una prenotazione basata sull'ID della richiesta di carica

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE Charge_Request ALTER COLUMN id RESTART WITH 1;\n", nativeQuery = true)
    void resetAutoIncrement();
}

