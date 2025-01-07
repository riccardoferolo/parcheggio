package Principale.Parcheggio.Controllers;

import Principale.Parcheggio.Models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import Principale.Parcheggio.Models.ChargeRequest;
import Principale.Parcheggio.Services.ChargeRequestService;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

 /*
    "percentuale_inizio": 20,
  "percentuale_fine": 80,
  "nome": "MarioRossi"
     */

@RestController
@RequestMapping("/chargerequests")
public class ChargeRequestController {

    @Autowired
    private ChargeRequestService chargeRequestService;

    @PostMapping("/new")
    public ResponseEntity<?> createChargeRequest(@RequestBody Map<String, Object> requestBody) {
        try {
            String nome = (String) requestBody.get("username"); // Deve essere una stringa

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd"); // Specifica il formato corretto
            String dataStr = (String) requestBody.get("giorno");
            LocalDate data;
            try {
                data = LocalDate.parse(dataStr, dateFormatter);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Errore nella data : " + e.getMessage());

            }

            String oraStr = (String) requestBody.get("ora");
            Time ora = Time.valueOf(oraStr); // Conversione in Time

            Integer percentuale_iniziale = (Integer) requestBody.get("Percentuale_iniziale"); // Deve essere un intero

            Integer percentuale_richiesta = (Integer) requestBody.get("Percentuale_richiesta"); // Deve essere un intero


            String dStr = (String) requestBody.get("durata");
            Time durata = Time.valueOf(dStr); // Conversione in Time

            String FStr = (String) requestBody.get("oraFine");
            Time oraFine = Time.valueOf(FStr); // Conversione in Time

            String Targa = (String) requestBody.get("Targa");

            Boolean r = (Boolean) requestBody.get("Ricarica");

            // Crea e salva la richiesta di carica
            ChargeRequest chargeRequest = chargeRequestService.createChargeRequest(nome, data, ora, durata, percentuale_iniziale, percentuale_richiesta, oraFine, Targa, r);
            return ResponseEntity.ok(chargeRequest);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Errore del server interno : " + e.getMessage());
        }
    }

    // Ottieni tutte le ChargeRequest
    @GetMapping("/all")
    public List<ChargeRequest> getAllChargeRequests() {
        return chargeRequestService.getAllChargeRequests();
    }

    // Ottieni ChargeRequest per ID
    @GetMapping("/charge-per-id")
    public ResponseEntity<ChargeRequest> getChargeRequestById(@RequestBody Long id) {
        Optional<ChargeRequest> chargeRequest = chargeRequestService.getChargeRequestById(id);
        return chargeRequest.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Cancella ChargeRequest per ID
    @DeleteMapping("/delete-per-id")
    public ResponseEntity<String> deleteChargeRequest(@RequestBody Long id) {
        chargeRequestService.deleteChargeRequest(id);
        return ResponseEntity.ok("la richiesta con id:" + id + " è stata eliminata");
    }

    @GetMapping("/user/trova-per-userid")
    public ResponseEntity<?> getRequestsByUserId(@RequestBody Long Userid) {
        try {
            List<ChargeRequest> chargeRequests = chargeRequestService.getChargeRequestsByUserId(Userid);

            // Controlla se ci sono richieste associate all'utente
            if (chargeRequests.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nessuna richiesta trovata per l'id " + Userid);
            }

            return ResponseEntity.ok(chargeRequests);

        } catch (Exception e) {
            // Gestisce eventuali errori durante il processo
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore del server interno: " + e.getMessage());
        }
    }

    // Endpoint per eliminare tutte le richieste
    @DeleteMapping("/delete-all")
    public ResponseEntity<String> eliminaTutteleRichieste() {
        try {
            chargeRequestService.eliminaTuttelerichieste();
            return ResponseEntity.ok("Tutte le richieste sono state eliminate");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore durante l'eliminazione delle richieste: " + e.getMessage());
        }
    }

    // Endpoint per cancellare tutte le richieste associate a un ID utente
    @DeleteMapping("/delete/user/id")
    public ResponseEntity<String> deleteRequestsByUserId(@RequestBody Long userId) {
        try {
            chargeRequestService.deleteRequestsByUserId(userId);
            return ResponseEntity.ok("Tutte le richieste per l'utente con ID " + userId + " sono state cancellate.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la cancellazione delle richieste: " + e.getMessage());
        }
    }

}

