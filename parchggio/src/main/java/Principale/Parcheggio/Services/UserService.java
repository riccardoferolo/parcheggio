package Principale.Parcheggio.Services;

import Principale.Parcheggio.Models.Ruolo;
import Principale.Parcheggio.Models.User;
import Principale.Parcheggio.Repository.UserRepository;
import Principale.Parcheggio.Repository.ChargeRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ChargeRequestRepository chargeRequestRepository;

    @Autowired
    public UserService(UserRepository userRepository, ChargeRequestRepository chargeRequestRepository) {
        this.userRepository = userRepository;
        this.chargeRequestRepository = chargeRequestRepository;
    }

    // Salva un nuovo utente
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Metodo per trovare un utente per username
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Metodo per trovare utenti per email
    public Optional<User> findUsersByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Metodo per trovare utenti con un username parziale
    public List<User> findUsersByUsernameContaining(String usernamePart) {
        return userRepository.findByUsernameContaining(usernamePart);
    }



    public void registerUser(User user) throws IllegalArgumentException {
        // Verifica se l'email esiste già
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email già registrata");
        }

        // Verifica se l'username esiste già
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username già esistente");
        }

        // Validazione della password
        validatePassword(user.getPassword());

        // Autocompila il ruolo e lascia che il database gestisca l'ID
        if (user.getRuolo() == null) {
            user.setRuolo(Ruolo.BASE); // Ruolo predefinito
        }

        // Imposta il saldo iniziale a 0 se non è stato specificato
        if (user.getSaldo() == 0) {
            user.setSaldo(0.0); // Imposta un saldo iniziale di 0
        }

        userRepository.save(user);
    }

    public void aggiornaSaldo(int userId, double nuovoSaldo) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        User user = userOptional.get();
        user.setSaldo(nuovoSaldo); // Correggi a setSaldo
        userRepository.save(user);
    }


    // Metodo per validare la password
    private void validatePassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("La password deve essere di almeno 8 caratteri");
        }
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("La password deve contenere almeno un numero");
        }
        if (!password.matches(".*[a-zA-Z].*")) {
            throw new IllegalArgumentException("La password deve contenere almeno una lettera");
        }
        if (!password.matches(".*[!@#\\$%\\^&\\*].*")) {
            throw new IllegalArgumentException("La password deve contenere almeno un carattere speciale (!@#$%^&*)");
        }
    }

    // Metodo per verificare se l'utente esiste (login)
    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Email non trovata"));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Password errata");
        }
        return user;
    }

    // Funzione per eliminare tutti gli utenti
    public void eliminaTuttiGliUtenti() {
        System.out.println("Eliminazione di tutti gli utenti dal database");
        userRepository.deleteAll();
        // Reset del contatore auto-increment per PostgreSQL
        userRepository.resetAutoIncrement();
        System.out.println("Contatore degli ID resettato a 1");
        System.out.println("Tutti gli utenti sono stati eliminati");
    }

    // Elimina un utente in base all'username e tutte le sue richieste di carica associate
    public void deleteUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            chargeRequestRepository.deleteByUserId((long) userOptional.get().getId());
            userRepository.deleteByUsername(userOptional.get().getUsername());
        } else {
            throw new IllegalArgumentException("Utente non trovato");
        }
    }

    public User getUserById(long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato con ID: " + userId));
    }


}

