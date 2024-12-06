import Principale.Parcheggio.Models.Ruolo;
import Principale.Parcheggio.Models.User;
import Principale.Parcheggio.Repository.UserRepository;
import Principale.Parcheggio.Repository.ChargeRequestRepository;
import Principale.Parcheggio.Services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestUserService {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChargeRequestRepository chargeRequestRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void quandoSaveUser_alloraSalvaUtente() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        User risultato = userService.saveUser(user);

        assertThat(risultato).isNotNull();
        verify(userRepository).save(user);
    }

    @Test
    void quandoFindUserByUsername_alloraRestituisciUtente() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> risultato = userService.findUserByUsername(username);

        assertThat(risultato).isPresent();
        assertThat(risultato.get().getUsername()).isEqualTo(username);
    }

    @Test
    void quandoRegisterUser_emailEsiste_alloraLanciaEccezione() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email già registrata");
    }

    @Test
    void quandoRegisterUser_usernameEsiste_alloraLanciaEccezione() {
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username già esistente");
    }

    @Test
    void quandoRegisterUser_passwordInvalida_alloraLanciaEccezione() {
        User user = new User();
        user.setPassword("short");

        assertThatThrownBy(() -> userService.registerUser(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La password deve essere di almeno 8 caratteri");
    }

    @Test
    void quandoAggiornaSaldo_utentetEsiste_alloraAggiornaSaldo() {
        int userId = 1;
        User user = new User();
        user.setId(userId);
        user.setSaldo(100.0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.aggiornaSaldo(userId, 200.0);

        assertThat(user.getSaldo()).isEqualTo(200.0);
        verify(userRepository).save(user);
    }

    @Test
    void quandoAggiornaSaldo_utenteNonEsiste_alloraLanciaEccezione() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.aggiornaSaldo(userId, 200.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Utente non trovato");
    }

    @Test
    void quandoLoginUser_emailEsiste_passwordCorretta_alloraRestituisciUtente() {
        String email = "test@example.com";
        String password = "Password1!";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User risultato = userService.loginUser(email, password);

        assertThat(risultato).isNotNull();
        assertThat(risultato.getEmail()).isEqualTo(email);
    }

    @Test
    void quandoLoginUser_passwordErrata_alloraLanciaEccezione() {
        String email = "test@example.com";
        String password = "Password1!";
        User user = new User();
        user.setEmail(email);
        user.setPassword("differentPassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.loginUser(email, password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password errata");
    }

    @Test
    void quandoEliminaTuttiGliUtenti_alloraEliminaTuttiEResettaAutoIncrement() {
        doNothing().when(userRepository).deleteAll();
        doNothing().when(userRepository).resetAutoIncrement();

        userService.eliminaTuttiGliUtenti();

        verify(userRepository).deleteAll();
        verify(userRepository).resetAutoIncrement();
    }

    @Test
    void quandoDeleteUserByUsername_utenteEsiste_alloraEliminaUtenteERelativeRichieste() {
        String username = "testUser";
        User user = new User();
        user.setId(1);
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        userService.deleteUserByUsername(username);

        verify(chargeRequestRepository).deleteByUserId((long) user.getId());
        verify(userRepository).deleteByUsername(username);
    }

    @Test
    void quandoDeleteUserByUsername_utenteNonEsiste_alloraLanciaEccezione() {
        String username = "nonExistingUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUserByUsername(username))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Utente non trovato");
    }

    @Test
    void quandoGetUserById_userEsiste_alloraRestituisciUser() {
        long userId = 1L;
        User user = new User();
        user.setId((int) userId);

        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));

        User risultato = userService.getUserById(userId);

        assertThat(risultato).isNotNull();
        assertThat(risultato.getId()).isEqualTo(userId);
    }

    @Test
    void quandoGetUserById_userNonEsiste_alloraLanciaEccezione() {
        long userId = 1L;
        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Utente non trovato con ID: " + userId);
    }
}

