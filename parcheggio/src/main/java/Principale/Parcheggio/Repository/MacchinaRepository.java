package Principale.Parcheggio.Repository;

import Principale.Parcheggio.Models.Macchine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MacchinaRepository extends JpaRepository<Macchine, String> {
    List<Macchine> findByUserId(int userId);
    Macchine findByTarga(String targa);

    // Estrae tutte le macchine di un determinato utente con kwBatteria = 0
    @Query("SELECT m FROM Macchine m WHERE m.user.id = :userId AND m.kwBatteria > 0")
    List<Macchine> findAllByUserIdElettriche(@Param("userId") Integer userId);

}

