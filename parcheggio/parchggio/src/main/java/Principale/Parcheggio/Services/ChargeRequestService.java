package Principale.Parcheggio.Services;

import Principale.Parcheggio.Models.Payment;
import Principale.Parcheggio.Models.User;
import Principale.Parcheggio.Repository.PaymentRepository;
import Principale.Parcheggio.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import Principale.Parcheggio.Models.ChargeRequest;
import Principale.Parcheggio.Repository.ChargeRequestRepository;


import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;


@Service
public class ChargeRequestService {

    @Autowired
    private ChargeRequestRepository chargeRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ParkingSpotService parkingSpotService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private PaymentRepository paymentRepository;

    private static final Logger logger = LoggerFactory.getLogger(ChargeRequestService.class);


    @Transactional
    public ChargeRequest createChargeRequest(String nome, LocalDate giorno, Time ora, Time durata, Integer Percentuale) {
        try {
            ChargeRequest chargeRequest = new ChargeRequest();
            chargeRequest.setPagare(calculateTotalAmount(ora, durata));

            // Cerca l'utente per nome
            Optional<User> userOptional = userRepository.findByUsername(nome);
            if (!userOptional.isPresent()) {
                throw new IllegalArgumentException("Utente non trovato");
            }

            User user = userOptional.get();

            // Controlla il saldo dell'utente
            if (user.getSaldo() < chargeRequest.getPagare()) {
                throw new IllegalArgumentException("Saldo insufficiente per completare il pagamento. Impossibile mandare la richiesta, prima ricaricare il saldo.");
            }

            // Scala il saldo e salva l'utente aggiornato
            user.setSaldo(user.getSaldo() - chargeRequest.getPagare());
            userService.saveUser(user);

            chargeRequest.setOra(ora);
            chargeRequest.setdurata(durata);
            chargeRequest.setGiorno(giorno);
            chargeRequest.setOra(ora);
            chargeRequest.setUser(user);
            chargeRequest.setpercentuale(Percentuale);


            // Imposta l'importo da pagare
            chargeRequest.setPagare(chargeRequest.getPagare());

            // Trova un parcheggio libero
            int idpark = parkingSpotService.getAndMarkRandomFreeParkingSpot();

            // Se non c'è parcheggio libero, lancia l'eccezione
            if (idpark == 0) {
                throw new IllegalArgumentException("Nessun parcheggio disponibile");
            }

            // Salva la richiesta di carica nel database
            ChargeRequest savedRequest = chargeRequestRepository.save(chargeRequest);

            // Crea il pagamento
            paymentService.createPayment(chargeRequest);

            // Recupera il pagamento associato all'ID della ChargeRequest
            Payment payment = paymentRepository.findByChargeRequestId(chargeRequest.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Pagamento non trovato per la richiesta di carica con ID " + chargeRequest.getId()));


            // Crea la prenotazione associata
            reservationService.addReservation(user.getId(), chargeRequest.getId(), idpark, payment);

            return savedRequest;
        } catch (IllegalArgumentException e) {
            // Log dell'errore e restituzione di una risposta HTTP 404 senza stack trace
            logger.error("Errore: " + e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        } catch (ResponseStatusException e) {
            // L'eccezione viene rilanciata, ma solo con il messaggio desiderato
            throw e; // Il client riceverà solo il messaggio HTTP senza stack trace aggiuntivi
        } catch (Exception e) {
            // Log dell'errore generico e restituzione di un errore 500
            logger.error("Errore durante la creazione della richiesta di carica: " + e.getMessage());
            throw new RuntimeException("Errore durante la creazione della richiesta di carica: " + e.getMessage());
        }
        }


    // Trova una ChargeRequest per ID
    public Optional<ChargeRequest> getChargeRequestById(Long id) {
        return chargeRequestRepository.findById(id);
    }

    // Trova tutte le ChargeRequest
    public List<ChargeRequest> getAllChargeRequests() {
        return chargeRequestRepository.findAll();
    }


    // Cancella una ChargeRequest per ID
    public void deleteChargeRequest(Long id) {
        chargeRequestRepository.deleteById(id);
        System.out.println("richiesta di carica con id" + id + "eliminata");
    }

    public List<ChargeRequest> getChargeRequestsByUserId(long userId) {
        // Recupera l'utente tramite l'ID
        User user = userService.getUserById(userId);

        // Usa il repository per trovare le ChargeRequest associate a quell'utente
        return chargeRequestRepository.findByUser(user);
    }


    // Funzione per eliminare tutte le richieste
    public void eliminaTuttelerichieste() {
        System.out.println("Eliminazione di tutte le richieste dal database");
        chargeRequestRepository.deleteAll();
        // Reset del contatore auto-increment per PostgreSQL
        chargeRequestRepository.resetAutoIncrement();
        System.out.println("Contatore degli ID resettato a 1");
        System.out.println("Tutte le richieste sono stati eliminate");
    }

    // Metodo per cancellare tutte le richieste associate a un ID utente
    @Transactional
    public void deleteRequestsByUserId(Long userId) {
        chargeRequestRepository.deleteByUserId(userId);
    }

    // Metodo per trovare tutte le richieste di carica associate a un ID utente
    public List<ChargeRequest> getRequestsByUserId(Long userid) {
        return chargeRequestRepository.findByUserId(userid);
    }

    // Calcolo del totale
    public double calculateTotalAmount(Time ora, Time durata) {
        double ore =  durata.getHours() * 60;
        double minuti = ore + durata.getMinutes();
        return minuti * 0.50;
    }
}

