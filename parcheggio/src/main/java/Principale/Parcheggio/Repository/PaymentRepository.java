package Principale.Parcheggio.Repository;

import Principale.Parcheggio.Models.Ruolo;
import org.springframework.data.jpa.repository.JpaRepository;
import Principale.Parcheggio.Models.Payment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE Payment ALTER COLUMN id RESTART WITH 1;\n", nativeQuery = true)
    void resetAutoIncrement();

    // Cancella tutte le richieste associate a un ID utente
    @Modifying
    @Transactional
    @Query("DELETE FROM Payment p WHERE p.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    Optional<Payment> findByChargeRequestId(Long chargeRequestId);

    // Ricerca pagamenti per tipo di prenotazione (sosta/ricarica)
    @Query("SELECT p FROM Payment p " +
            "JOIN p.chargeRequest cr " +
            "WHERE (:ricarica = true AND cr.ricarica = true) " +
            "   OR (:ricarica = false AND cr.ricarica IS NULL)")
    List<Payment> findPaymentsByType(@Param("ricarica") Boolean ricarica);

    // Ricerca pagamenti per ruolo dell'utente (base/premium)
    @Query("SELECT p FROM Payment p " +
            "JOIN p.user u " +
            "WHERE u.ruolo = :ruolo")
    List<Payment> findPaymentsByUserRole(@Param("ruolo") Ruolo ruolo);


}

