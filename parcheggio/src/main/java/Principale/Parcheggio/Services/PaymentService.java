package Principale.Parcheggio.Services;

import Principale.Parcheggio.Models.*;
import Principale.Parcheggio.Repository.*;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserService userService;  // Uso del servizio UserService esistente

    // Metodo per trovare un pagamento tramite ID
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento non trovato con ID: " + id));
    }

    public Payment createPayment(ChargeRequest chargeRequest) {
        // Verifica che l'utente abbia saldo sufficiente
        User user = chargeRequest.getUser();
        double totalAmount = chargeRequest.getPagare();

        // Crea il pagamento
        Payment payment = new Payment();
        payment.setChargeRequest(chargeRequest);
        payment.setUser(user);
        payment.setPaid(true);
        payment.setTotalAmount(totalAmount);

        // Salva il pagamento nel repository
        return paymentRepository.save(payment);
    }

    // Funzione per eliminare tutte le richieste
    public void eliminaTuttiPagamenti() {
        System.out.println("Eliminazione di tutti i pagamenti dal database");
        paymentRepository.deleteAll();
        // Reset del contatore auto-increment per PostgreSQL
        paymentRepository.resetAutoIncrement();
        System.out.println("Contatore degli ID resettato a 1");
        System.out.println("Tutti i pagamenti sono stati cancellati");
    }

    // Pagamenti per tipo di prenotazione
    public List<Payment> getPaymentsByType(Boolean ricarica) {
        return paymentRepository.findPaymentsByType(ricarica);
    }

    // Pagamenti per ruolo dell'utente
    public List<Payment> getPaymentsByUserRole(Ruolo ruolo) {
        return paymentRepository.findPaymentsByUserRole(ruolo);
    }

    // Metodo per cancellare tutte le richieste associate a un ID utente
    /*
    @Transactional
    public void eliminaPagamentoperUserId(Long userId) {
        paymentRepository.deleteByUserId(userId);


    }

     */

}

