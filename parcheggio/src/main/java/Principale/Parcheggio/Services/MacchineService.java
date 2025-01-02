package Principale.Parcheggio.Services;

import Principale.Parcheggio.Models.Macchine;
import Principale.Parcheggio.Models.User;
import Principale.Parcheggio.Repository.MacchinaRepository;
import Principale.Parcheggio.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MacchineService {

    @Autowired
    private MacchinaRepository macchineRepository;

    @Autowired
    private UserRepository userRepository;

    // Creare una nuova macchina
    public Macchine createMacchina(String targa, double kwBatteria, String modello, int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Macchine macchina = new Macchine(targa, kwBatteria, modello, user);
        return macchineRepository.save(macchina);
    }

    // Cancellare una macchina
    public void deleteMacchina(String targa) {
        Macchine macchina = macchineRepository.findByTarga(targa);
        if (macchina != null) {
            macchineRepository.delete(macchina);
        } else {
            throw new RuntimeException("Macchina non trovata");
        }
    }

    // Modificare i parametri di una macchina
    public Macchine updateMacchina(String targa, double kwBatteria, String modello) {
        Macchine macchina = macchineRepository.findByTarga(targa);
        if (macchina != null) {
            macchina.setKwBatteria(kwBatteria);
            macchina.setModello(modello);
            return macchineRepository.save(macchina);
        } else {
            throw new RuntimeException("Macchina non trovata");
        }
    }

    // Restituire tutte le macchine per un determinato id utente
    public List<Macchine> getMacchineByUserId(int userId) {
        return macchineRepository.findByUserId(userId);
    }

    // Restituire i dettagli di una macchina tramite targa
    public Macchine getMacchinaByTarga(String targa) {
        return macchineRepository.findByTarga(targa);
    }

    // Funzione per verificare la validità della targa italiana
    public static boolean isValidTargaItaliana(String targa) {
        // RegEx per formato attuale
        String formatoAttuale = "^[A-Z]{2}[0-9]{3}[A-Z]{2}$";

        // RegEx per formato storico
        String formatoStorico = "^[A-Z]{1,2}[0-9]{1,6}$";

        // Verifica che la targa rispetti almeno uno dei formati
        return targa.matches(formatoAttuale) || targa.matches(formatoStorico);
    }


    public void deleteMacchine(){
        System.out.println("eliminazione di tutte le macchine dal database");
        macchineRepository.deleteAll();
        System.out.println("tutte le macchine sono state eliminate");
    }

}

