package Principale.Parcheggio.Controllers;

import Principale.Parcheggio.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import Principale.Parcheggio.Models.ChargeRequest;
import Principale.Parcheggio.Services.ChargeRequestService;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
            String nome = (String) requestBody.get("nome");

            // Converte il campo "giorno" da stringa in formato italiano "dd/MM/yyyy" a LocalDate
            String giornoStr = (String) requestBody.get("giorno");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ITALIAN);
            LocalDate giorno = LocalDate.parse(giornoStr, formatter);

            // Converte il campo "ora" da stringa a Time (se necessario)
            String oraStr = (String) requestBody.get("ora");
            SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
            Time ora = new Time(timeFormatter.parse(oraStr).getTime());

            // Converte il campo "ora" da stringa a Time (se necessario)
            String durataStr = (String) requestBody.get("durata");
            Time durata = new Time(timeFormatter.parse(durataStr).getTime());

            // Crea e salva la richiesta di carica
            ChargeRequest chargeRequest = chargeRequestService.createChargeRequest(nome, giorno, ora, durata);
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
    @GetMapping("/{id}")
    public ResponseEntity<ChargeRequest> getChargeRequestById(@PathVariable Long id) {
        Optional<ChargeRequest> chargeRequest = chargeRequestService.getChargeRequestById(id);
        return chargeRequest.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Cancella ChargeRequest per ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChargeRequest(@PathVariable Long id) {
        chargeRequestService.deleteChargeRequest(id);
        return ResponseEntity.ok("la richiesta con id:" + id + " è stata eliminata");
    }

    @GetMapping("/user/{Userid}")
    public ResponseEntity<?> getRequestsByUserId(@PathVariable Long Userid) {
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
    @DeleteMapping("/delete/user/{userId}")
    public ResponseEntity<String> deleteRequestsByUserId(@PathVariable Long userId) {
        try {
            chargeRequestService.deleteRequestsByUserId(userId);
            return ResponseEntity.ok("Tutte le richieste per l'utente con ID " + userId + " sono state cancellate.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la cancellazione delle richieste: " + e.getMessage());
        }
    }


}

