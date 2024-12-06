package TestModels;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import Principale.Parcheggio.Models.*;

class ChargeRequestTest {

    private ChargeRequest chargeRequest;

    @BeforeEach
    void setUp() {
        chargeRequest = new ChargeRequest();
    }

    @Test
    void testSetAndGetId() {
        chargeRequest.setId(1L);
        assertEquals(1L, chargeRequest.getId());
    }

    @Test
    void testSetAndGetGiorno() {
        LocalDate date = LocalDate.of(2023, 10, 25);
        chargeRequest.setGiorno(date);
        assertEquals(date, chargeRequest.getGiorno());
    }

    @Test
    void testSetAndGetDurata() {
        Time durata = Time.valueOf("01:30:00");
        chargeRequest.setdurata(durata);
        assertEquals(durata, chargeRequest.getdurata());
    }

    @Test
    void testSetAndGetOra() {
        Time ora = Time.valueOf("08:45:00");
        chargeRequest.setOra(ora);
        assertEquals(ora, chargeRequest.getOra());
    }

    @Test
    void testSetAndGetUser() {
        User user = new User();
        user.setId(1); // supponendo che User abbia un campo id
        chargeRequest.setUser(user);
        assertEquals(user, chargeRequest.getUser());
    }

    @Test
    void testSetAndGetPagare() {
        chargeRequest.setPagare(99.99);
        assertEquals(99.99, chargeRequest.getPagare());
    }
}

