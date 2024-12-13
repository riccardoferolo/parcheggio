package TestModels;

import Principale.Parcheggio.Models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestReservation {

    private Reservation reservation;
    private User user;
    private ChargeRequest chargeRequest;
    private Payment payment;
    private ParkingSpot parkingSpot;

    @BeforeEach
    void setUp() {
        // Crea istanze simulate per i test
        user = new User();
        user.setId(1); // Supponiamo che User abbia un campo id

        chargeRequest = new ChargeRequest();
        chargeRequest.setId(1L); // Supponiamo che ChargeRequest abbia un campo id

        payment = new Payment();
        payment.setId(1L); // Supponiamo che Payment abbia un campo id

        parkingSpot = new ParkingSpot();
        parkingSpot.setId(1); // Supponiamo che ParkingSpot abbia un campo id

        // Inizializza l'istanza di Reservation
        reservation = new Reservation();
    }

    @Test
    void testSetAndGetId() {
        reservation.setId(1L);
        assertEquals(1L, reservation.getId());
    }

    @Test
    void testSetAndGetUser() {
        reservation.setUser(user);
        assertEquals(user, reservation.getUser(), "The user should match the set user.");
    }

    @Test
    void testSetAndGetChargeRequest() {
        reservation.setChargeRequest(chargeRequest);
        assertEquals(chargeRequest, reservation.getChargeRequest(), "The chargeRequest should match the set chargeRequest.");
    }

    @Test
    void testSetAndGetPayment() {
        reservation.setPayment(payment);
        assertEquals(payment, reservation.getPayment(), "The payment should match the set payment.");
    }

    @Test
    void testSetAndGetParkingSpot() {
        reservation.setParkingSpot(parkingSpot);
        assertEquals(parkingSpot, reservation.getParkingSpot(), "The parking spot should match the set parking spot.");
    }
}

