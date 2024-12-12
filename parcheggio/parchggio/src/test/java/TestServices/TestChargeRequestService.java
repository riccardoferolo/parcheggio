package TestServices;

import Principale.Parcheggio.Models.*;
import Principale.Parcheggio.Repository.ChargeRequestRepository;
import Principale.Parcheggio.Repository.PaymentRepository;
import Principale.Parcheggio.Repository.UserRepository;
import Principale.Parcheggio.Services.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestChargeRequestService {

    private static final String USERNAME_TEST = "utenteTest";
    private static final LocalDate GIORNO_TEST = LocalDate.now();
    private static final Time ORA_TEST = Time.valueOf("10:00:00");
    private static final Time DURATA_TEST = Time.valueOf("01:00:00");
    private static final double SALDO_SUFFICIENTE = 100.0;
    private static final double SALDO_INSUFFICIENTE = 10.0;
    private static final double IMPORTO_DA_PAGARE = 50.0;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private ParkingSpotService parkingSpotService;
    @Mock
    private ChargeRequestRepository chargeRequestRepository;
    @Mock
    private PaymentService paymentService;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private ReservationService reservationService;
    @Mock
    private Logger logger;

    @Spy
    @InjectMocks
    private ChargeRequestService chargeRequestService;

    @Test
    void quandoUtenteNonTrovato_alloraLanciaEccezione() {
        when(userRepository.findByUsername(USERNAME_TEST)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> chargeRequestService.createChargeRequest(USERNAME_TEST, GIORNO_TEST, ORA_TEST, DURATA_TEST))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Utente non trovato");
    }

    @Test
    void quandoSaldoInsufficiente_alloraLanciaEccezione() {
        User user = new User();
        user.setSaldo(SALDO_INSUFFICIENTE);
        when(userRepository.findByUsername(USERNAME_TEST)).thenReturn(Optional.of(user));
        when(chargeRequestService.calculateTotalAmount(ORA_TEST, DURATA_TEST)).thenReturn(IMPORTO_DA_PAGARE);

        assertThatThrownBy(() -> chargeRequestService.createChargeRequest(USERNAME_TEST, GIORNO_TEST, ORA_TEST, DURATA_TEST))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Saldo insufficiente per completare il pagamento. Impossibile mandare la richiesta, prima ricaricare il saldo.");
    }

    @Test
    void quandoParcheggioNonDisponibile_alloraLanciaEccezione() {
        User user = new User();
        user.setSaldo(SALDO_SUFFICIENTE);
        when(userRepository.findByUsername(USERNAME_TEST)).thenReturn(Optional.of(user));
        when(parkingSpotService.getAndMarkRandomFreeParkingSpot()).thenReturn(0);

        assertThatThrownBy(() -> chargeRequestService.createChargeRequest(USERNAME_TEST, GIORNO_TEST, ORA_TEST, DURATA_TEST))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nessun parcheggio disponibile");
    }

    @Test
    void quandoRichiestaDiCaricaCreataConSuccesso_alloraSalvaChargeRequest() {
        User user = new User();
        user.setSaldo(SALDO_SUFFICIENTE);
        ChargeRequest chargeRequest = new ChargeRequest();
        Payment payment = new Payment();
        payment.setId(1L);

        when(userRepository.findByUsername(USERNAME_TEST)).thenReturn(Optional.of(user));
        when(parkingSpotService.getAndMarkRandomFreeParkingSpot()).thenReturn(1);
        when(chargeRequestRepository.save(any(ChargeRequest.class))).thenReturn(chargeRequest);
        when(paymentRepository.findByChargeRequestId(any())).thenReturn(Optional.of(payment));

        ChargeRequest risultato = chargeRequestService.createChargeRequest(USERNAME_TEST, GIORNO_TEST, ORA_TEST, DURATA_TEST);

        assertThat(risultato).isNotNull();
        verify(userService).saveUser(any(User.class));
        verify(parkingSpotService).getAndMarkRandomFreeParkingSpot();
        verify(chargeRequestRepository).save(any(ChargeRequest.class));
        verify(paymentService).createPayment(any(ChargeRequest.class));
        verify(reservationService).addReservation(anyLong(), anyLong(), anyInt(), any(Payment.class));
    }

    @Test
    void quandoChargeRequestEsiste_alloraRestituisciChargeRequest() {
        Long idTest = 1L;
        ChargeRequest chargeRequest = new ChargeRequest();
        chargeRequest.setId(idTest);

        when(chargeRequestRepository.findById(idTest)).thenReturn(Optional.of(chargeRequest));

        Optional<ChargeRequest> risultato = chargeRequestService.getChargeRequestById(idTest);

        assertThat(risultato).isPresent();
        assertThat(risultato.get().getId()).isEqualTo(idTest);
        verify(chargeRequestRepository).findById(idTest);
    }

    @Test
    void quandoChargeRequestNonEsiste_alloraRestituisciOptionalVuoto() {
        Long idTest = 2L;

        when(chargeRequestRepository.findById(idTest)).thenReturn(Optional.empty());

        Optional<ChargeRequest> risultato = chargeRequestService.getChargeRequestById(idTest);

        assertThat(risultato).isNotPresent();
        verify(chargeRequestRepository).findById(idTest);
    }

    @Test
    void quandoGetAllChargeRequests_alloraRestituisciListaChargeRequest() {
        ChargeRequest chargeRequest1 = new ChargeRequest();
        ChargeRequest chargeRequest2 = new ChargeRequest();
        List<ChargeRequest> listaDiTest = Arrays.asList(chargeRequest1, chargeRequest2);

        when(chargeRequestRepository.findAll()).thenReturn(listaDiTest);

        List<ChargeRequest> risultato = chargeRequestService.getAllChargeRequests();

        assertThat(risultato).hasSize(2);
        assertThat(risultato).containsExactly(chargeRequest1, chargeRequest2);
        verify(chargeRequestRepository).findAll();
    }

    @Test
    void quandoDeleteChargeRequest_alloraEliminaChargeRequest() {
        Long idTest = 3L;

        doNothing().when(chargeRequestRepository).deleteById(idTest);

        chargeRequestService.deleteChargeRequest(idTest);

        verify(chargeRequestRepository).deleteById(idTest);
    }

    @Test
    void quandoGetChargeRequestsByUserId_alloraRestituisciListaChargeRequests() {
        long userIdTest = 1L;
        User user = new User();
        user.setId((int) userIdTest);

        ChargeRequest chargeRequest1 = new ChargeRequest();
        ChargeRequest chargeRequest2 = new ChargeRequest();
        List<ChargeRequest> listaDiTest = Arrays.asList(chargeRequest1, chargeRequest2);

        when(userService.getUserById(userIdTest)).thenReturn(user);
        when(chargeRequestRepository.findByUser(user)).thenReturn(listaDiTest);

        List<ChargeRequest> risultato = chargeRequestService.getChargeRequestsByUserId(userIdTest);

        assertThat(risultato).hasSize(2);
        assertThat(risultato).containsExactly(chargeRequest1, chargeRequest2);
        verify(userService).getUserById(userIdTest);
        verify(chargeRequestRepository).findByUser(user);
    }

    @Test
    void quandoEliminaTuttelerichieste_alloraEliminaTutteLeRichieste() {
        doNothing().when(chargeRequestRepository).deleteAll();
        doNothing().when(chargeRequestRepository).resetAutoIncrement();

        chargeRequestService.eliminaTuttelerichieste();

        verify(chargeRequestRepository).deleteAll();
        verify(chargeRequestRepository).resetAutoIncrement();
    }

    @Test
    void quandoDeleteRequestsByUserId_alloraEliminaLeRichiesteDellUtente() {
        Long userIdTest = 1L;

        doNothing().when(chargeRequestRepository).deleteByUserId(userIdTest);

        chargeRequestService.deleteRequestsByUserId(userIdTest);

        verify(chargeRequestRepository).deleteByUserId(userIdTest);
    }

    @Test
    void quandoGetRequestsByUserId_alloraRestituisciListaChargeRequests() {
        Long userIdTest = 1L;
        ChargeRequest chargeRequest1 = new ChargeRequest();
        ChargeRequest chargeRequest2 = new ChargeRequest();
        List<ChargeRequest> listaDiTest = Arrays.asList(chargeRequest1, chargeRequest2);

        when(chargeRequestRepository.findByUserId(userIdTest)).thenReturn(listaDiTest);

        List<ChargeRequest> risultato = chargeRequestService.getRequestsByUserId(userIdTest);

        assertThat(risultato).hasSize(2);
        assertThat(risultato).containsExactly(chargeRequest1, chargeRequest2);
        verify(chargeRequestRepository).findByUserId(userIdTest);
    }

    @Test
    void quandoCalculateTotalAmount_alloraCalcolaImportoTotaleCorretto() {
        Time oraTest = Time.valueOf("10:00:00");
        Time durataTest = Time.valueOf("01:30:00"); // 1 ora e 30 minuti
        double importoAtteso = 90 * 0.50; // Totale in minuti * 0.50

        double risultato = chargeRequestService.calculateTotalAmount(oraTest, durataTest);

        assertThat(risultato).isEqualTo(importoAtteso);
    }
}


