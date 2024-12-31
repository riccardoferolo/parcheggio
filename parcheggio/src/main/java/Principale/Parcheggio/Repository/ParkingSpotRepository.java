package Principale.Parcheggio.Repository;

import Principale.Parcheggio.Models.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Integer> {
    // Puoi aggiungere query personalizzate se necessario
}