package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.*;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateAirportStatusUseCaseImplTest {

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private UpdateAirportStatusUseCaseImpl useCase;

    private Airport airport;

    @BeforeEach
    void setUp() {
        airport = new Airport(
                new IATACode("LIS"),
                new AirportDetails("Humberto Delgado", "Lisbon", "Portugal",
                        "Europe", "Europe/Lisbon", new Coordinates(38.77, -9.13)),
                AirportState.OPERATIONAL,
                List.of(new Runway("03/21", 3805.0, "030/210"))
        );
    }

    @Test
    void updates_status_for_valid_transition() {
        when(airportRepository.findById(new IATACode("LIS"))).thenReturn(Optional.of(airport));
        when(airportRepository.save(any(Airport.class))).thenAnswer(i -> i.getArgument(0));

        Airport result = useCase.updateStatus("LIS", "CLOSED");

        assertEquals(AirportState.CLOSED, result.getStatus());
    }

    @Test
    void throws_not_found_for_missing_airport() {
        when(airportRepository.findById(any(IATACode.class))).thenReturn(Optional.empty());

        assertThrows(AirportNotFoundException.class,
                () -> useCase.updateStatus("FAO", "CLOSED"));
    }

    @Test
    void throws_illegal_argument_for_unknown_state_without_db_access() {
        // Fail-fast: valueOf fires before findById — no DB stub needed.
        assertThrows(IllegalArgumentException.class,
                () -> useCase.updateStatus("LIS", "FLYING"));

        verifyNoInteractions(airportRepository);
    }

    @Test
    void throws_invalid_transition_when_same_state() {
        when(airportRepository.findById(new IATACode("LIS"))).thenReturn(Optional.of(airport));

        assertThrows(InvalidStatusTransitionException.class,
                () -> useCase.updateStatus("LIS", "OPERATIONAL"));
    }

    @Test
    void state_string_is_uppercased_before_parsing() {
        when(airportRepository.findById(new IATACode("LIS"))).thenReturn(Optional.of(airport));
        when(airportRepository.save(any(Airport.class))).thenAnswer(i -> i.getArgument(0));

        Airport result = useCase.updateStatus("LIS", "closed");

        assertEquals(AirportState.CLOSED, result.getStatus());
    }
}
