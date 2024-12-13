package TestModels;

import Principale.Parcheggio.Models.ParkingSpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestParkingSpot {

    private ParkingSpot parkingSpot;

    @BeforeEach
    void setUp() {
        parkingSpot = new ParkingSpot();
    }

    @Test
    void testSetAndGetId() {
        parkingSpot.setId(1);
        assertEquals(1, parkingSpot.getId());
    }

    @Test
    void testDefaultIsOccupied() {
        // Verifica che il valore di default di isOccupied sia false
        assertFalse(parkingSpot.isOccupied());
    }

    @Test
    void testSetOccupied() {
        parkingSpot.setOccupied();
        assertTrue(parkingSpot.isOccupied(), "The parking spot should be occupied.");
    }

    @Test
    void testSetAvailable() {
        parkingSpot.setOccupied(); // Imposta come occupato prima
        parkingSpot.setAvailable(); // Imposta come disponibile
        assertFalse(parkingSpot.isOccupied(), "The parking spot should be available.");
    }
}

