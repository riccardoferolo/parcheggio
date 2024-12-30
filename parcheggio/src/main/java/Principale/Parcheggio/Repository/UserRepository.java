package Principale.Parcheggio.Repository;

import Principale.Parcheggio.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    Optional<User> findUserById(long id);

    // Trova un utente in base all'username
    Optional<User> findByUsername(String username);


    // Trova tutti gli utenti che hanno una certa email
    Optional<User> findByEmail(String email);

    // Trova utenti il cui username contiene una certa stringa (ricerca parziale)
    List<User> findByUsernameContaining(String usernamePart);



    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE users ALTER COLUMN id RESTART WITH 1;\n", nativeQuery = true)
    void resetAutoIncrement();

    // Cancella tutte le richieste associate a un ID utente
    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.username = :username")
    void deleteByUsername(@Param("username") String username);
}

