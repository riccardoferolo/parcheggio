package Principale.Parcheggio.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Principale.Parcheggio.Models.*;
import Principale.Parcheggio.Services.*;
import org.springframework.http.HttpStatus;


import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ChargeRequestService chargeRequestService;  // Servizio per recuperare ChargeRequest

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody Long chargeRequestId) {
        try {
            // Recupera la ChargeRequest usando il suo ID
            Optional<ChargeRequest> chargeRequestOpt = chargeRequestService.getChargeRequestById(chargeRequestId);

            // Verifica se la ChargeRequest esiste
            if (!chargeRequestOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ChargeRequest con ID " + chargeRequestId + " non trovata.");
            }

            ChargeRequest chargeRequest = chargeRequestOpt.get();

            // Crea il pagamento basato su questa ChargeRequest
            Payment payment = paymentService.createPayment(chargeRequest);

            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            // Gestione delle eccezioni e log
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la creazione del pagamento: " + e.getMessage());
        }
    }


    // Recupera un pagamento tramite ID
    @GetMapping("/recuper_id")
    public ResponseEntity<Payment> getPaymentById(@RequestBody Long id) {
        Payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }

    // Endpoint per eliminare tutti gli utenti
    @DeleteMapping("/delete-all")
    public ResponseEntity<String> eliminaTuttiPagamenti() {
        try {
            paymentService.eliminaTuttiPagamenti();
            return ResponseEntity.ok("tutti i pagamenti sono stati eliminati");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore durante l'eliminazione dei pagamenti: " + e.getMessage());
        }
    }

    @PostMapping("/listaPagamenti")
    public ResponseEntity<List<Payment>> getPayments(@RequestBody Map<String, Object> params) {
        Boolean ricarica = (Boolean) params.get("ricarica");
        String ruolo = (String) params.get("ruolo");

        if (ricarica != null) {
            // Filtra per tipo di prenotazione (ricarica/sosta)
            return ResponseEntity.ok(paymentService.getPaymentsByType(ricarica));
        } else if (ruolo != null) {
            try {
                Ruolo userRole = Ruolo.valueOf(ruolo.toUpperCase());
                // Filtra per ruolo dell'utente (base/premium)
                return ResponseEntity.ok(paymentService.getPaymentsByUserRole(userRole));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /*
    // Endpoint per cancellare tutti i pagamenti associati a un ID utente
    @DeleteMapping("/delete/user/id")
    public ResponseEntity<String> deleteRequestsByUserId(@RequestBody Long userId) {
        try {
            paymentService.eliminaPagamentoperUserId(userId);
            return ResponseEntity.ok("Tutti i pagamenti  per l'utente con ID " + userId + " sono stati cancellati.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la cancellazione delle richieste: " + e.getMessage());
        }
    }

     */

}

