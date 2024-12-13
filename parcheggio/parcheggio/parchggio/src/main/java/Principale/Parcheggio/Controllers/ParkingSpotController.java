package Principale.Parcheggio.Controllers;

import Principale.Parcheggio.Models.ParkingSpot;
import Principale.Parcheggio.Services.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parking")
public class ParkingSpotController {

    @Autowired
    private ParkingSpotService parkingSpotService;

    // Endpoint per creare un nuovo parcheggio (disponibile)
    @PostMapping("/create")
    public ResponseEntity<ParkingSpot> createParkingSpot() {
        ParkingSpot newSpot = parkingSpotService.createParkingSpot();
        return ResponseEntity.ok(newSpot);
    }

    // Endpoint per marcare un parcheggio come disponibile
    @PutMapping("/free/{id}")
    public ResponseEntity<String> freeParkingSpot(@PathVariable int id) {
        try {
            ParkingSpot updatedSpot = parkingSpotService.markAsAvailable(id);
            return ResponseEntity.ok("Parcheggio con ID " + id + " marcato come disponibile.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint per eliminare tutte le richieste
    @DeleteMapping("/delete-all")
    public ResponseEntity<String> eliminaTutteleRichieste() {
        try {
            parkingSpotService.eliminaTuttiParcheggi();
            return ResponseEntity.ok("Tutti i parcheggi sono stati eliminati");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore durante l'eliminazione dei parcheggi: " + e.getMessage());
        }
    }
}

