package Principale.Parcheggio.Services;

import Principale.Parcheggio.Models.*;
import Principale.Parcheggio.Repository.ChargeRequestRepository;
import Principale.Parcheggio.Repository.ReservationRepository;
import Principale.Parcheggio.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChargeRequestRepository chargeRequestRepository;


    @Transactional
    public Reservation addReservation(long userId, long chargeRequestId, Payment payment, String Targa, Boolean Ricarica) {
        logger.info("Tentativo di aggiungere una prenotazione: User ID: {}, ChargeRequest ID: {}", userId, chargeRequestId);

        try {
            // Recupera l'utente in base all'ID
            User user = userRepository.findUserById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Utente con ID " + userId + " non trovato"));
            logger.info("Utente recuperato: {}", user.getUsername());

            // Recupera la richiesta di carica in base all'ID
            ChargeRequest chargeRequest = chargeRequestRepository.findById(chargeRequestId)
                    .orElseThrow(() -> new IllegalArgumentException("Richiesta di carica con ID " + chargeRequestId + " non trovata"));
            logger.info("Richiesta di carica recuperata: {}", chargeRequest.getId());


            // Crea una nuova prenotazione
            Reservation reservation = new Reservation();
            reservation.setUser(user);
            reservation.setChargeRequest(chargeRequest);
            reservation.setTarga(Targa);
            reservation.setRicarica(Ricarica);

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
        return reservationRepository.findAllByRicaricaTrue();
    }

    public Map<String, Integer> Occupazione() {
        List<Object[]> results = reservationRepository.Occupazione();

        // Inizializza i contatori
        int totaleRicariche = 0;
        int totaleSoste = 0;

        // Somma i totali da tutti i gruppi
        for (Object[] row : results) {
            totaleRicariche += ((Number) row[2]).intValue(); // Colonna ricariche
            totaleSoste += ((Number) row[3]).intValue();     // Colonna soste
        }

        // Restituisci i totali in una mappa
        Map<String, Integer> summary = new HashMap<>();
        summary.put("ricariche", totaleRicariche);
        summary.put("soste", totaleSoste);
        return summary;
    }
}

