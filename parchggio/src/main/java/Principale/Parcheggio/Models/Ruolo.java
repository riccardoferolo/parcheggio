package Principale.Parcheggio.Models;

public enum Ruolo {
    ADMIN,
    PREMIUM,
    BASE;

    // Metodo per ottenere un Ruolo da una stringa
    public static Ruolo fromValue(String value) {
        for (Ruolo role : Ruolo.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Valore non valido per Ruolo: " + value);
    }

    // Metodo per verificare se un ruolo esiste
    public static boolean exists(String value) {
        for (Ruolo role : Ruolo.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}






