package Principale.Parcheggio.Controllers;

import Principale.Parcheggio.Models.User;
import Principale.Parcheggio.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Trova un utente per username
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findUserByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint per la registrazione di un nuovo utente
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok("Utente registrato con successo");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint per il login dell'utente
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        try {
            User user = userService.loginUser(email, password);
            return ResponseEntity.ok("Login effettuato con successo");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint per aggiornare il saldo di un utente
    @PutMapping("/{id}/saldo")
    public ResponseEntity<String> aggiornaSaldo(@PathVariable int id, @RequestBody Map<String, Double> requestBody) {
        double nuovoSaldo = requestBody.get("saldo");

        try {
            userService.aggiornaSaldo(id, nuovoSaldo);
            return ResponseEntity.ok("Saldo aggiornato con successo");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore del server interno");
        }
    }

    // Endpoint per eliminare tutti gli utenti
    @DeleteMapping("/delete-all")
    public ResponseEntity<String> eliminaTuttiGliUtenti() {
        try {
            userService.eliminaTuttiGliUtenti();
            return ResponseEntity.ok("Tutti gli utenti sono stati eliminati");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore durante l'eliminazione degli utenti: " + e.getMessage());
        }
    }

    // Endpoint per cancellare un utente in base all'username
    @DeleteMapping("/delete/user/{username}")
    public ResponseEntity<String> deleteUserByUsername(@PathVariable String username) {
        try {
            userService.deleteUserByUsername(username);
            return ResponseEntity.ok("Utente con username " + username + " cancellato con successo.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la cancellazione: " + e.getMessage());
        }
    }

}

