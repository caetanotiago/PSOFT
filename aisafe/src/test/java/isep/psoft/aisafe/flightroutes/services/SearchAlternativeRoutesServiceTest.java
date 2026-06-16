package isep.psoft.aisafe.flightroutes.services;

import isep.psoft.aisafe.airports.repositories.AirportRepository;
import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
import isep.psoft.aisafe.flightroutes.domain.Itinerary;
import isep.psoft.aisafe.flightroutes.repositories.FlightRouteRepository;
import isep.psoft.aisafe.flightroutes.services.strategy.RouteSearchStrategy;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchAlternativeRoutesServiceTest {

    @Mock private FlightRouteRepository flightRouteRepository;
    @Mock private AirportRepository airportRepository;
    @Mock private RouteSearchStrategy routeSearchStrategy;

    @InjectMocks private SearchAlternativeRoutesService service;

    @Test
    void ensureThrows404WhenOriginAirportMissing() {
        when(airportRepository.existsById(any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> service.searchAlternatives("LIS", "MAD"));
        verify(routeSearchStrategy, never()).search(anyList(), anyString(), anyString());
    }

    @Test
    void ensureThrows404WhenDestinationAirportMissing() {
        // origem existe, destino não
        when(airportRepository.existsById(any())).thenReturn(true, false);

        assertThrows(EntityNotFoundException.class, () -> service.searchAlternatives("LIS", "MAD"));
        verify(routeSearchStrategy, never()).search(anyList(), anyString(), anyString());
    }

    @Test
    void ensureDelegatesToStrategyWithUppercasedCodes() {
        when(airportRepository.existsById(any())).thenReturn(true);
        List<FlightRoute> active = List.of(mock(FlightRoute.class));
        when(flightRouteRepository.findAllActive()).thenReturn(active);
        Itinerary itinerary = new Itinerary(List.of(mock(FlightRoute.class, RETURNS_DEEP_STUBS)));
        when(routeSearchStrategy.search(eq(active), eq("LIS"), eq("MAD"))).thenReturn(List.of(itinerary));

        ArgumentCaptor<String> origin = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> dest = ArgumentCaptor.forClass(String.class);

        List<Itinerary> result = service.searchAlternatives("lis", "mad");

        assertEquals(1, result.size());
        verify(routeSearchStrategy).search(eq(active), origin.capture(), dest.capture());
        assertEquals("LIS", origin.getValue());
        assertEquals("MAD", dest.getValue());
    }
}
