package Principale.Parcheggio.Services;

import Principale.Parcheggio.DTO.ReservationDTO;
import Principale.Parcheggio.Models.*;
import Principale.Parcheggio.Repository.ChargeRequestRepository;
import Principale.Parcheggio.Repository.ParkingSpotRepository;
import Principale.Parcheggio.Repository.ReservationRepository;
import Principale.Parcheggio.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChargeRequestRepository chargeRequestRepository;

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    @Transactional
    public Reservation addReservation(long userId, long chargeRequestId, int parkingSpotId, Payment payment) {
        logger.info("Tentativo di aggiungere una prenotazione: User ID: {}, ChargeRequest ID: {}, ParkingSpot ID: {}", userId, chargeRequestId, parkingSpotId);

        try {
            // Recupera l'utente in base all'ID
            User user = userRepository.findUserById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Utente con ID " + userId + " non trovato"));
            logger.info("Utente recuperato: {}", user.getUsername());

            // Recupera la richiesta di carica in base all'ID
            ChargeRequest chargeRequest = chargeRequestRepository.findById(chargeRequestId)
                    .orElseThrow(() -> new IllegalArgumentException("Richiesta di carica con ID " + chargeRequestId + " non trovata"));
            logger.info("Richiesta di carica recuperata: {}", chargeRequest.getId());

            // Recupera il parcheggio in base all'ID
            ParkingSpot parkingSpot = parkingSpotRepository.findById(parkingSpotId)
                    .orElseThrow(() -> new IllegalArgumentException("Parcheggio con ID " + parkingSpotId + " non trovato"));
            logger.info("Parcheggio recuperato: {}", parkingSpot.getId());

            // Crea una nuova prenotazione
            Reservation reservation = new Reservation();
            reservation.setUser(user);
            reservation.setParkingSpot(parkingSpot);
            reservation.setChargeRequest(chargeRequest);

            // Associa il pagamento alla prenotazione
            if (payment == null) {
                throw new IllegalArgumentException("Il pagamento non può essere nullo.");
            }
            reservation.setPayment(payment);  // Associa l'oggetto Payment
            logger.info("Prenotazione creata con pagamento, tentativo di salvataggio nel database.");

            // Salva la prenotazione nel database
            Reservation savedReservation = reservationRepository.save(reservation);
            logger.info("Prenotazione salvata con successo, ID: {}", savedReservation.getId());

            return savedReservation;

        } catch (IllegalArgumentException e) {
            logger.error("Errore di validazione nei parametri: {}", e.getMessage());
            throw new RuntimeException("Errore nei parametri forniti: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Errore durante la creazione della prenotazione: {}", e.getMessage());
            throw new RuntimeException("Errore interno del server: " + e.getMessage());
        }
    }



    // Cancella una prenotazione per ID
    public boolean deleteReservationById(long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            reservationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Cancella tutte le prenotazioni
    public void deleteAllReservations() {
        reservationRepository.deleteAll();
        reservationRepository.resetAutoIncrement();
    }

    // Cancella una prenotazione in base all'ID della richiesta di carica
    public boolean deleteReservationByChargeRequestId(long chargeRequestId) {
        Optional<Reservation> reservation = reservationRepository.findByChargeRequestId(chargeRequestId);
        if (reservation.isPresent()) {
            reservationRepository.delete(reservation.get());
            return true;
        }
        return false;
    }

    // Trova tutte le prenotazioni (utile per debugging o future funzioni)
    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }
}

