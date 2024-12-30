package Principale.Parcheggio.Repository;

import Principale.Parcheggio.Models.Macchine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MacchinaRepository extends JpaRepository<Macchine, String> {
    List<Macchine> findByUserId(int userId);
    Macchine findByTarga(String targa);
}

