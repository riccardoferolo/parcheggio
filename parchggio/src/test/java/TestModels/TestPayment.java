package TestModels;

import Principale.Parcheggio.Models.ChargeRequest;
import Principale.Parcheggio.Models.Payment;
import Principale.Parcheggio.Models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestPayment {

    private Payment payment;
    private ChargeRequest chargeRequest;
    private User user;

    @BeforeEach
    void setUp() {
        // Crea una simulazione di ChargeRequest e User per i test
        chargeRequest = new ChargeRequest();
        chargeRequest.setPagare(100.0); // Imposta un importo di esempio

        user = new User();
        user.setId(1); // Supponiamo che User abbia un campo id

        // Inizializza l'istanza di Payment con costruttore
        payment = new Payment(chargeRequest, user);
    }

    @Test
    void testDefaultConstructor() {
        Payment payment = new Payment();
        assertNotNull(payment, "Payment instance should be created");
    }

    @Test
    void testParameterizedConstructor() {
        assertEquals(chargeRequest, payment.getChargeRequest(), "ChargeRequest should match");
        assertEquals(user, payment.getUser(), "User should match");
        assertFalse(payment.isPaid(), "Payment should be unpaid by default");
        assertEquals(100.0, payment.getTotalAmount(), "Total amount should match chargeRequest's amount");
    }

    @Test
    void testSetAndGetId() {
        payment.setId(1L);
        assertEquals(1L, payment.getId());
    }

    @Test
    void testSetAndGetChargeRequest() {
        ChargeRequest newChargeRequest = new ChargeRequest();
        newChargeRequest.setPagare(200.0);
        payment.setChargeRequest(newChargeRequest);
        assertEquals(newChargeRequest, payment.getChargeRequest());
    }

    @Test
    void testSetAndGetUser() {
        User newUser = new User();
        newUser.setId(2);
        payment.setUser(newUser);
        assertEquals(newUser, payment.getUser());
    }

    @Test
    void testSetAndGetIsPaid() {
        payment.setPaid(true);
        assertTrue(payment.isPaid(), "Payment should be marked as paid");
    }

    @Test
    void testSetAndGetTotalAmount() {
        payment.setTotalAmount(150.0);
        assertEquals(150.0, payment.getTotalAmount());
    }
}

