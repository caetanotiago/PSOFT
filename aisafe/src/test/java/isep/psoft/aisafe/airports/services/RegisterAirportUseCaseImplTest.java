package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.*;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterAirportUseCaseImplTest {

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private RegisterAirportUseCaseImpl useCase;

    private static final List<Runway> RUNWAYS = List.of(new Runway("03/21", 3805.0, "030/210"));

    @Test
    void registers_airport_successfully() {
        when(airportRepository.existsById(any(IATACode.class))).thenReturn(false);
        when(airportRepository.save(any(Airport.class))).thenAnswer(i -> i.getArgument(0));

        Airport result = useCase.registerAirport(
                "LIS", "Humberto Delgado", "Lisbon", "Portugal",
                "Europe", "Europe/Lisbon", 38.77, -9.13, RUNWAYS);

        assertEquals("LIS", result.getIataCode().getCode());
        assertEquals(AirportState.OPERATIONAL, result.getStatus());
        verify(airportRepository).save(any(Airport.class));
    }

    @Test
    void throws_duplicate_when_iata_already_exists() {
        when(airportRepository.existsById(any(IATACode.class))).thenReturn(true);

        assertThrows(DuplicateIATACodeException.class, () ->
                useCase.registerAirport(
                        "LIS", "Humberto Delgado", "Lisbon", "Portugal",
                        "Europe", "Europe/Lisbon", 38.77, -9.13, RUNWAYS));

        verify(airportRepository, never()).save(any());
    }

    @Test
    void iata_code_is_uppercased_before_lookup() {
        when(airportRepository.existsById(new IATACode("LIS"))).thenReturn(false);
        when(airportRepository.save(any(Airport.class))).thenAnswer(i -> i.getArgument(0));

        Airport result = useCase.registerAirport(
                "lis", "Lisbon", "Lisbon", "Portugal",
                "Europe", "Europe/Lisbon", 38.77, -9.13, RUNWAYS);

        assertEquals("LIS", result.getIataCode().getCode());
    }
}
