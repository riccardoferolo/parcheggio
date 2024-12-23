package Principale.Parcheggio.Repository;

import Principale.Parcheggio.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import Principale.Parcheggio.Models.ChargeRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ChargeRequestRepository extends JpaRepository<ChargeRequest, Long> {

    
    List<ChargeRequest> findByUser(User user);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE Charge_Request ALTER COLUMN id RESTART WITH 1;\n", nativeQuery = true)
    void resetAutoIncrement();

    // Cancella tutte le richieste associate a un ID utente
    @Modifying
    @Transactional
    @Query("DELETE FROM ChargeRequest cr WHERE cr.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    List<ChargeRequest> findByUserId(Long userid);

    void deleteById(Long id);
}

