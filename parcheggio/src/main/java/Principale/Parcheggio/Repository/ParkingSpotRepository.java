package Principale.Parcheggio.Repository;

import Principale.Parcheggio.Models.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Integer> {
    // Puoi aggiungere query personalizzate se necessario
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE Parking_spot ALTER COLUMN id RESTART WITH 1;\n", nativeQuery = true)
    void resetAutoIncrement();

}