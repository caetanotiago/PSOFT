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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewAirportDetailsUseCaseImplTest {

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private ViewAirportDetailsUseCaseImpl useCase;

    private Airport lisbon;

    @BeforeEach
    void setUp() {
        lisbon = new Airport(
                new IATACode("LIS"),
                new AirportDetails("Humberto Delgado", "Lisbon", "Portugal",
                        "Europe", "Europe/Lisbon", new Coordinates(38.77, -9.13)),
                AirportState.OPERATIONAL,
                List.of(new Runway("03/21", 3805.0, "030/210"))
        );
    }

    @Test
    void returns_airport_when_found() {
        when(airportRepository.findById(new IATACode("LIS"))).thenReturn(Optional.of(lisbon));
        Airport result = useCase.getAirportByIataCode("LIS");
        assertEquals("LIS", result.getIataCode().getCode());
    }

    @Test
    void throws_not_found_when_airport_missing() {
        when(airportRepository.findById(any(IATACode.class))).thenReturn(Optional.empty());
        assertThrows(AirportNotFoundException.class, () -> useCase.getAirportByIataCode("FAO"));
    }

    @Test
    void throws_illegal_argument_for_invalid_iata_format() {
        assertThrows(IllegalArgumentException.class, () -> useCase.getAirportByIataCode("invalid"));
    }
}
