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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchAirportsUseCaseImplTest {

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private SearchAirportsUseCaseImpl useCase;

    private Airport lisbon;
    private Airport porto;

    @BeforeEach
    void setUp() {
        lisbon = new Airport(
                new IATACode("LIS"),
                new AirportDetails("Humberto Delgado", "Lisbon", "Portugal",
                        "Europe", "Europe/Lisbon", new Coordinates(38.77, -9.13)),
                AirportState.OPERATIONAL, List.of(new Runway("03/21", 3805.0, "030/210")));
        porto = new Airport(
                new IATACode("OPO"),
                new AirportDetails("Francisco Sá Carneiro", "Porto", "Portugal",
                        "Europe", "Europe/Lisbon", new Coordinates(41.24, -8.68)),
                AirportState.OPERATIONAL, List.of(new Runway("18/36", 3480.0, "180/360")));
    }

    @Test
    void returns_matching_airports_by_city() {
        when(airportRepository.searchByCriteria(null, "Lisbon", null))
                .thenReturn(List.of(lisbon));

        List<Airport> result = useCase.searchAirports(null, "Lisbon", null);

        assertEquals(1, result.size());
        assertEquals("LIS", result.get(0).getIataCode().getCode());
    }

    @Test
    void returns_all_airports_when_no_criteria() {
        when(airportRepository.searchByCriteria(null, null, null))
                .thenReturn(List.of(lisbon, porto));

        List<Airport> result = useCase.searchAirports(null, null, null);

        assertEquals(2, result.size());
    }

    @Test
    void returns_empty_list_when_no_matches() {
        when(airportRepository.searchByCriteria(null, "Tokyo", null))
                .thenReturn(List.of());

        List<Airport> result = useCase.searchAirports(null, "Tokyo", null);

        assertTrue(result.isEmpty());
    }

    @Test
    void returns_airports_matching_country() {
        when(airportRepository.searchByCriteria(null, null, "Portugal"))
                .thenReturn(List.of(lisbon, porto));

        List<Airport> result = useCase.searchAirports(null, null, "Portugal");

        assertEquals(2, result.size());
    }
}
