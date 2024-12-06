package Principale.Parcheggio.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Principale.Parcheggio.Models.*;
import Principale.Parcheggio.Services.*;
import org.springframework.http.HttpStatus;


import java.util.Optional;


@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ChargeRequestService chargeRequestService;  // Servizio per recuperare ChargeRequest

    @PostMapping("/create/{chargeRequestId}")
    public ResponseEntity<?> createPayment(@PathVariable Long chargeRequestId) {
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
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
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

    // Endpoint per cancellare tutti i pagamenti associati a un ID utente
    @DeleteMapping("/delete/user/{userId}")
    public ResponseEntity<String> deleteRequestsByUserId(@PathVariable Long userId) {
        try {
            paymentService.eliminaPagamentoperUserId(userId);
            return ResponseEntity.ok("Tutti i pagamenti  per l'utente con ID " + userId + " sono stati cancellati.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la cancellazione delle richieste: " + e.getMessage());
        }
    }

}

