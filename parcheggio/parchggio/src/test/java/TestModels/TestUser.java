package TestModels;

import Principale.Parcheggio.Models.Ruolo;
import Principale.Parcheggio.Models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestUser {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testSetAndGetId() {
        user.setId(1);
        assertEquals(1, user.getId(), "The ID should match the set ID.");
    }

    @Test
    void testSetAndGetUsername() {
        user.setUsername("testuser");
        assertEquals("testuser", user.getUsername(), "The username should match the set username.");
    }

    @Test
    void testSetAndGetPassword() {
        user.setPassword("password123");
        assertEquals("password123", user.getPassword(), "The password should match the set password.");
    }

    @Test
    void testSetAndGetEmail() {
        user.setEmail("testuser@example.com");
        assertEquals("testuser@example.com", user.getEmail(), "The email should match the set email.");
    }

    @Test
    void testSetAndGetSaldo() {
        user.setSaldo(100.50);
        assertEquals(100.50, user.getSaldo(), "The saldo should match the set saldo.");
    }

    @Test
    void testSetAndGetRuolo() {
        Ruolo ruolo = Ruolo.ADMIN; // Supponendo che ci sia un enum Ruolo con valore ADMIN
        user.setRuolo(ruolo);
        assertEquals(ruolo, user.getRuolo(), "The ruolo should match the set ruolo.");
    }
}

