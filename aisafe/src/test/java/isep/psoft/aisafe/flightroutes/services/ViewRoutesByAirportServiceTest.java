package isep.psoft.aisafe.flightroutes.services;

import isep.psoft.aisafe.airports.domain.*;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import isep.psoft.aisafe.flightroutes.domain.EstimatedFlightTime;
import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
import isep.psoft.aisafe.flightroutes.domain.RouteDistance;
import isep.psoft.aisafe.flightroutes.domain.RouteRequirements;
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
class ViewRoutesByAirportServiceTest {

    @Mock
    private AirportRepository airportRepository;

    @Mock
    private FlightRouteRepository flightRouteRepository;

    @InjectMocks
    private ViewRoutesByAirportService service;

    private Airport lisbon;
    private Airport porto;

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
    }

    @Test
    void returns_routes_where_airport_is_origin_or_destination() {
        FlightRoute lisToOpo = new FlightRoute(lisbon, porto,
                new RouteDistance(312.0), new RouteRequirements(500.0, 50), new EstimatedFlightTime(60));
        FlightRoute opoToLis = new FlightRoute(porto, lisbon,
                new RouteDistance(312.0), new RouteRequirements(500.0, 50), new EstimatedFlightTime(60));

        when(airportRepository.existsById(new IATACode("LIS"))).thenReturn(true);
        when(flightRouteRepository.findByOriginOrDestination("LIS")).thenReturn(List.of(lisToOpo, opoToLis));

        List<FlightRoute> result = service.findRoutesByAirport("LIS");

        assertEquals(2, result.size());
    }

    @Test
    void returns_empty_list_when_airport_has_no_routes() {
        when(airportRepository.existsById(new IATACode("LIS"))).thenReturn(true);
        when(flightRouteRepository.findByOriginOrDestination("LIS")).thenReturn(List.of());

        List<FlightRoute> result = service.findRoutesByAirport("LIS");

        assertTrue(result.isEmpty());
    }

    @Test
    void throws_airport_not_found_for_unknown_iata() {
        when(airportRepository.existsById(new IATACode("XXX"))).thenReturn(false);

        assertThrows(AirportNotFoundException.class, () -> service.findRoutesByAirport("XXX"));
        verify(flightRouteRepository, never()).findByOriginOrDestination(any());
    }

    @Test
    void uppercases_iata_code_before_querying() {
        when(airportRepository.existsById(new IATACode("LIS"))).thenReturn(true);
        when(flightRouteRepository.findByOriginOrDestination("LIS")).thenReturn(List.of());

        service.findRoutesByAirport("lis");

        verify(flightRouteRepository).findByOriginOrDestination("LIS");
    }
}
