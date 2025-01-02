package Principale.Parcheggio.Controllers;

import Principale.Parcheggio.Models.Macchine;
import Principale.Parcheggio.Models.User;
import Principale.Parcheggio.Repository.MacchinaRepository;
import Principale.Parcheggio.Repository.UserRepository;
import Principale.Parcheggio.Services.MacchineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/macchine")
public class MacchineController {

    @Autowired
    private MacchineService macchineService;
    @Autowired
    private MacchinaRepository macchinaRepository;
    @Autowired
    private UserRepository userRepository;

    // Creare una nuova macchina
    @PostMapping("/create")
    public ResponseEntity<Macchine> createMacchina(@RequestParam String targa, @RequestParam double kwBatteria, @RequestParam String modello, @RequestParam int userId) {
        Macchine macchina = macchineService.createMacchina(targa, kwBatteria, modello, userId);
        return new ResponseEntity<>(macchina, HttpStatus.CREATED);
    }

    // Cancellare una macchina
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteMacchina(@RequestParam String targa) {
        macchineService.deleteMacchina(targa);
        return ResponseEntity.noContent().build();
    }

    // Cancellare una macchina
    @DeleteMapping("/deleteALL")
    public ResponseEntity<Void> deleteMacchine() {
        macchineService.deleteMacchine();
        return ResponseEntity.noContent().build();
    }

    // Modificare i parametri di una macchina
    @PutMapping("/update")
    public ResponseEntity<Macchine> updateMacchina(@RequestParam String targa, @RequestParam double kwBatteria, @RequestParam String modello) {
        Macchine macchina = macchineService.updateMacchina(targa, kwBatteria, modello);
        return ResponseEntity.ok(macchina);
    }

    // Restituire tutte le macchine per un determinato id utente
    @GetMapping("/byUser")
    public ResponseEntity<List<Macchine>> getMacchineByUserId(@RequestParam String username) {
        Optional<User> user = userRepository.findByUsername(username);
        List<Macchine> macchine = macchineService.getMacchineByUserId(user.get().getId());
        return ResponseEntity.ok(macchine);
    }

    // Restituire i dettagli di una macchina tramite targa
    @GetMapping("/byTarga")
    public ResponseEntity<Macchine> getMacchinaByTarga(@RequestParam String targa) {
        Macchine macchina = macchineService.getMacchinaByTarga(targa);
        if (macchina != null) {
            return ResponseEntity.ok(macchina);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

