package TestServices;

import Principale.Parcheggio.Models.ChargeRequest;
import Principale.Parcheggio.Models.Payment;
import Principale.Parcheggio.Models.User;
import Principale.Parcheggio.Repository.PaymentRepository;
import Principale.Parcheggio.Services.PaymentService;
import Principale.Parcheggio.Services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestPaymentService {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void quandoGetPaymentById_pagamentoEsiste_alloraRestituisciPayment() {
        Long paymentId = 1L;
        Payment payment = new Payment();
        payment.setId(paymentId);

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        Payment risultato = paymentService.getPaymentById(paymentId);

        assertThat(risultato).isNotNull();
        assertThat(risultato.getId()).isEqualTo(paymentId);
        verify(paymentRepository).findById(paymentId);
    }

    @Test
    void quandoGetPaymentById_pagamentoNonEsiste_alloraLanciaEccezione() {
        Long paymentId = 2L;

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.getPaymentById(paymentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Pagamento non trovato con ID: " + paymentId);
    }

    @Test
    void quandoCreatePayment_alloraSalvaPagamentoNelRepository() {
        ChargeRequest chargeRequest = new ChargeRequest();
        User user = new User();
        chargeRequest.setUser(user);
        chargeRequest.setPagare(100.0);

        Payment payment = new Payment();
        payment.setChargeRequest(chargeRequest);
        payment.setUser(user);
        payment.setPaid(true);
        payment.setTotalAmount(100.0);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment risultato = paymentService.createPayment(chargeRequest);

        assertThat(risultato).isNotNull();
        assertThat(risultato.isPaid()).isTrue();
        assertThat(risultato.getTotalAmount()).isEqualTo(100.0);
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void quandoEliminaTuttiPagamenti_alloraEliminaTuttiEResettaAutoIncrement() {
        doNothing().when(paymentRepository).deleteAll();
        doNothing().when(paymentRepository).resetAutoIncrement();

        paymentService.eliminaTuttiPagamenti();

        verify(paymentRepository).deleteAll();
        verify(paymentRepository).resetAutoIncrement();
    }

    @Test
    void quandoEliminaPagamentoperUserId_alloraEliminaPagamentiDellUtente() {
        Long userId = 3L;

        doNothing().when(paymentRepository).deleteByUserId(userId);

        paymentService.eliminaPagamentoperUserId(userId);

        verify(paymentRepository).deleteByUserId(userId);
    }
}

