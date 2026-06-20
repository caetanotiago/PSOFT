package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.*;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import isep.psoft.aisafe.flightroutes.repositories.FlightRouteRepository;
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
class ListBusiestAirportsUseCaseImplTest {

    @Mock
    private AirportRepository airportRepository;

    @Mock
    private FlightRouteRepository flightRouteRepository;

    @InjectMocks
    private ListBusiestAirportsUseCaseImpl useCase;

    private Airport lisbon;
    private Airport porto;
    private Airport faro;

    @BeforeEach
    void setUp() {
        lisbon = new Airport(new IATACode("LIS"),
                new AirportDetails("Humberto Delgado", "Lisbon", "Portugal", "Europe", "Europe/Lisbon",
                        new Coordinates(38.77, -9.13)),
                AirportState.OPERATIONAL, List.of(new Runway("03/21", 3805.0, "030/210")));
        porto = new Airport(new IATACode("OPO"),
                new AirportDetails("Francisco Sá Carneiro", "Porto", "Portugal", "Europe", "Europe/Lisbon",
                        new Coordinates(41.24, -8.68)),
                AirportState.OPERATIONAL, List.of(new Runway("18/36", 3480.0, "180/360")));
        faro = new Airport(new IATACode("FAO"),
                new AirportDetails("Faro", "Faro", "Portugal", "Europe", "Europe/Lisbon",
                        new Coordinates(37.01, -7.97)),
                AirportState.OPERATIONAL, List.of(new Runway("10/28", 2490.0, "100/280")));
    }

    @Test
    void merges_origin_and_destination_counts_per_airport() {
        when(airportRepository.findAll()).thenReturn(List.of(lisbon, porto, faro));
        when(flightRouteRepository.countByOrigin()).thenReturn(List.<Object[]>of(
                new Object[]{"LIS", 2L}));
        when(flightRouteRepository.countByDestination()).thenReturn(List.<Object[]>of(
                new Object[]{"LIS", 3L}, new Object[]{"OPO", 1L}));

        List<AirportRouteCount> result = useCase.listBusiest(null);

        AirportRouteCount lisCount = result.stream()
                .filter(c -> c.getAirport().getIataCode().getCode().equals("LIS")).findFirst().orElseThrow();
        assertEquals(5L, lisCount.getRouteCount());
    }

    @Test
    void includes_airports_with_zero_routes() {
        when(airportRepository.findAll()).thenReturn(List.of(lisbon, porto, faro));
        when(flightRouteRepository.countByOrigin()).thenReturn(List.of());
        when(flightRouteRepository.countByDestination()).thenReturn(List.of());

        List<AirportRouteCount> result = useCase.listBusiest(null);

        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(c -> c.getRouteCount() == 0));
    }

    @Test
    void sorts_descending_by_route_count() {
        when(airportRepository.findAll()).thenReturn(List.of(lisbon, porto, faro));
        when(flightRouteRepository.countByOrigin()).thenReturn(List.<Object[]>of(
                new Object[]{"LIS", 1L}, new Object[]{"OPO", 5L}, new Object[]{"FAO", 2L}));
        when(flightRouteRepository.countByDestination()).thenReturn(List.of());

        List<AirportRouteCount> result = useCase.listBusiest(null);

        assertEquals("OPO", result.get(0).getAirport().getIataCode().getCode());
        assertEquals("FAO", result.get(1).getAirport().getIataCode().getCode());
        assertEquals("LIS", result.get(2).getAirport().getIataCode().getCode());
    }

    @Test
    void respects_limit_parameter() {
        when(airportRepository.findAll()).thenReturn(List.of(lisbon, porto, faro));
        when(flightRouteRepository.countByOrigin()).thenReturn(List.of());
        when(flightRouteRepository.countByDestination()).thenReturn(List.of());

        List<AirportRouteCount> result = useCase.listBusiest(2);

        assertEquals(2, result.size());
    }

    @Test
    void throws_illegal_argument_for_non_positive_limit() {
        assertThrows(IllegalArgumentException.class, () -> useCase.listBusiest(0));
        assertThrows(IllegalArgumentException.class, () -> useCase.listBusiest(-1));
        verify(airportRepository, never()).findAll();
    }
}
