package Principale.Parcheggio.Repository;

import Principale.Parcheggio.Models.ParkingSpot;
import Principale.Parcheggio.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.nio.channels.FileChannel;
import java.util.Optional;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Integer> {
    // Metodo per trovare il primo parcheggio libero (non occupato)
    Optional<ParkingSpot> findFirstByIsOccupiedFalse();

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE PARKING_SPOTS ALTER COLUMN id RESTART WITH 1;\n", nativeQuery = true)
    void resetAutoIncrement();
}
