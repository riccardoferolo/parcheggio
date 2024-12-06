package Principale.Parcheggio.Services;

import Principale.Parcheggio.Models.ParkingSpot;
import Principale.Parcheggio.Repository.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Random;

@Service
public class ParkingSpotService {

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    // Metodo per inserire un nuovo parcheggio come disponibile
    public ParkingSpot createParkingSpot() {
        ParkingSpot newSpot = new ParkingSpot();
        newSpot.setAvailable();  // Imposta come disponibile
        return parkingSpotRepository.save(newSpot);
    }

    // Metodo per marcare un parcheggio come disponibile
    public ParkingSpot markAsAvailable(int id) throws IllegalArgumentException {
        Optional<ParkingSpot> spotOptional = parkingSpotRepository.findById(id);

        if (spotOptional.isPresent()) {
            ParkingSpot spot = spotOptional.get();
            spot.setAvailable();  // Imposta come disponibile
            return parkingSpotRepository.save(spot);
        } else {
            throw new IllegalArgumentException("Parcheggio non trovato");
        }
    }


    public int getAndMarkRandomFreeParkingSpot() {
        return parkingSpotRepository.findFirstByIsOccupiedFalse()
                .map(freeSpot -> {
                    freeSpot.setOccupied();
                    parkingSpotRepository.save(freeSpot); // Segna come occupato
                    return freeSpot.getId();
                })
                // Se non ci sono parcheggi liberi, lancia un'eccezione IllegalArgumentException
                .orElseThrow(() -> new IllegalArgumentException("Nessun parcheggio disponibile"));
    }

    // Funzione per eliminare tutte le richieste
    public void eliminaTuttiParcheggi() {
        System.out.println("Eliminazione di tutti i parcheggi dal database");
        parkingSpotRepository.deleteAll();
        // Reset del contatore auto-increment per PostgreSQL
        parkingSpotRepository.resetAutoIncrement();
        System.out.println("Contatore degli ID resettato a 1");
        System.out.println("Tutti i parcheggi sono stati cancellati");
    }

}

