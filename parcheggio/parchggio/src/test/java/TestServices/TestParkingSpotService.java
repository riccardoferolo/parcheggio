package TestServices;

import Principale.Parcheggio.Models.ParkingSpot;
import Principale.Parcheggio.Repository.ParkingSpotRepository;
import Principale.Parcheggio.Services.ParkingSpotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestParkingSpotService {

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @InjectMocks
    private ParkingSpotService parkingSpotService;

    @Test
    void quandoCreateParkingSpot_alloraRestituisciParkingSpotDisponibile() {
        ParkingSpot newSpot = new ParkingSpot();
        newSpot.setAvailable();

        when(parkingSpotRepository.save(any(ParkingSpot.class))).thenReturn(newSpot);

        ParkingSpot risultato = parkingSpotService.createParkingSpot();

        assertThat(risultato).isNotNull();
        assertThat(risultato.isOccupied()).isFalse();
        verify(parkingSpotRepository).save(any(ParkingSpot.class));
    }

    @Test
    void quandoMarkAsAvailable_parcheggioEsiste_alloraImpostaComeDisponibile() {
        int idTest = 1;
        ParkingSpot spot = new ParkingSpot();
        spot.setId(idTest);
        spot.setOccupied(); // Inizialmente impostato come occupato

        when(parkingSpotRepository.findById(idTest)).thenReturn(Optional.of(spot));
        when(parkingSpotRepository.save(any(ParkingSpot.class))).thenReturn(spot);

        ParkingSpot risultato = parkingSpotService.markAsAvailable(idTest);

        assertThat(risultato.isOccupied()).isFalse();
        verify(parkingSpotRepository).findById(idTest);
        verify(parkingSpotRepository).save(spot);
    }

    @Test
    void quandoMarkAsAvailable_parcheggioNonEsiste_alloraLanciaEccezione() {
        int idTest = 2;

        when(parkingSpotRepository.findById(idTest)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> parkingSpotService.markAsAvailable(idTest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Parcheggio non trovato");
    }

    @Test
    void quandoGetAndMarkRandomFreeParkingSpot_parcheggioLiberoEsiste_alloraRestituisciIdEImpostaComeOccupato() {
        ParkingSpot freeSpot = new ParkingSpot();
        freeSpot.setId(3);
        freeSpot.setAvailable(); // Parcheggio inizialmente libero

        when(parkingSpotRepository.findFirstByIsOccupiedFalse()).thenReturn(Optional.of(freeSpot));
        when(parkingSpotRepository.save(freeSpot)).thenReturn(freeSpot);

        int risultato = parkingSpotService.getAndMarkRandomFreeParkingSpot();

        assertThat(risultato).isEqualTo(freeSpot.getId());
        assertThat(freeSpot.isOccupied()).isTrue();
        verify(parkingSpotRepository).findFirstByIsOccupiedFalse();
        verify(parkingSpotRepository).save(freeSpot);
    }

    @Test
    void quandoGetAndMarkRandomFreeParkingSpot_nessunParcheggioLibero_alloraLanciaEccezione() {
        when(parkingSpotRepository.findFirstByIsOccupiedFalse()).thenReturn(Optional.empty());

        assertThatThrownBy(() -> parkingSpotService.getAndMarkRandomFreeParkingSpot())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nessun parcheggio disponibile");
    }

    @Test
    void quandoEliminaTuttiParcheggi_alloraEliminaTuttiEResettaAutoIncrement() {
        doNothing().when(parkingSpotRepository).deleteAll();
        doNothing().when(parkingSpotRepository).resetAutoIncrement();

        parkingSpotService.eliminaTuttiParcheggi();

        verify(parkingSpotRepository).deleteAll();
        verify(parkingSpotRepository).resetAutoIncrement();
    }
}

