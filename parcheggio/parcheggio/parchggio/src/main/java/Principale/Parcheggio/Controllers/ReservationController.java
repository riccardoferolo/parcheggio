package Principale.Parcheggio.Controllers;

import Principale.Parcheggio.Services.ReservationService;
import Principale.Parcheggio.DTO.ReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Principale.Parcheggio.Models.Reservation;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    // Cancella tutte le prenotazioni
    @DeleteMapping("/delete/all")
    public ResponseEntity<String> deleteAllReservations() {
        reservationService.deleteAllReservations();
        return ResponseEntity.ok("Tutte le prenotazioni sono state cancellate.");
    }

    // Cancella una prenotazione per ID della richiesta di carica
    @DeleteMapping("/delete/charge-request/{chargeRequestId}")
    public ResponseEntity<String> deleteReservationByChargeRequestId(@PathVariable long chargeRequestId) {
        boolean deleted = reservationService.deleteReservationByChargeRequestId(chargeRequestId);
        if (deleted) {
            return ResponseEntity.ok("Prenotazione cancellata con successo.");
        } else {
            return ResponseEntity.status(404).body("Prenotazione con ID della richiesta di carica non trovata.");
        }
    }

    // Trova tutte le prenotazioni (opzionale)
    @GetMapping("/all")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.findAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/coda")
    public ResponseEntity<List<ReservationDTO>> getProvaReservations() {
        List<Reservation> reservations = reservationService.findAllReservations();

        // Ordinamento per giorno e ora
        reservations.sort(Comparator
                .comparing((Reservation r) -> r.getChargeRequest().getGiorno())
                .thenComparing(r -> r.getChargeRequest().getOra()));

        // Converte in DTO usando Collectors.toList()
        List<ReservationDTO> reservationDTOs = reservations.stream()
                .map(reservation -> new ReservationDTO(
                        reservation.getId(),
                        reservation.getUser().getId(),
                        reservation.getChargeRequest().getId(),
                        reservation.getPayment().getId(),
                        reservation.getParkingSpot().getId(),
                        reservation.getChargeRequest().getGiorno(),
                        reservation.getChargeRequest().getOra(),
                        reservation.getChargeRequest().getdurata()
                ))
                .collect(Collectors.toList()); // Utilizzo di Collectors.toList()

        return ResponseEntity.ok(reservationDTOs);
    }

}

