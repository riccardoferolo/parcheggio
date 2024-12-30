package Principale.Parcheggio.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import Principale.Parcheggio.Models.Payment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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
}

