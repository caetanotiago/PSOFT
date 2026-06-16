package isep.psoft.aisafe.flightroutes.services.strategy;

import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
import isep.psoft.aisafe.flightroutes.domain.Itinerary;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MinStopsRouteSearchTest {

    private final MinStopsRouteSearch search = new MinStopsRouteSearch();

    private FlightRoute route(String origin, String dest, double km) {
        FlightRoute r = mock(FlightRoute.class, RETURNS_DEEP_STUBS);
        when(r.getOrigin().getIataCode().getCode()).thenReturn(origin);
        when(r.getDestination().getIataCode().getCode()).thenReturn(dest);
        when(r.getDistance().getDistance()).thenReturn(km);
        return r;
    }

    @Test
    void ensureDirectRouteFoundWithZeroStops() {
        List<FlightRoute> graph = List.of(route("LIS", "MAD", 500.0));

        List<Itinerary> result = search.search(graph, "LIS", "MAD");

        assertEquals(1, result.size());
        assertEquals(0, result.get(0).getNumberOfStops());
    }

    @Test
    void ensureOneStopItineraryFoundViaHub() {
        List<FlightRoute> graph = List.of(
                route("LIS", "OPO", 300.0),
                route("OPO", "MAD", 434.0));

        List<Itinerary> result = search.search(graph, "LIS", "MAD");

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getNumberOfStops());
        assertEquals(734.0, result.get(0).getTotalDistance());
    }

    @Test
    void ensureNoPathReturnsEmpty() {
        List<FlightRoute> graph = List.of(route("LIS", "OPO", 300.0));

        assertTrue(search.search(graph, "LIS", "MAD").isEmpty());
    }

    @Test
    void ensureDirectIsPreferredOverIndirect() {
        List<FlightRoute> graph = List.of(
                route("LIS", "MAD", 500.0),   // direta (0 stops)
                route("LIS", "OPO", 300.0),   // indireta...
                route("OPO", "MAD", 434.0));

        List<Itinerary> result = search.search(graph, "LIS", "MAD");

        // Só deve devolver a solução de menor nº de escalas (a direta)
        assertEquals(1, result.size());
        assertEquals(0, result.get(0).getNumberOfStops());
    }

    @Test
    void ensureCyclesDoNotCauseInfiniteLoop() {
        List<FlightRoute> graph = List.of(
                route("LIS", "OPO", 300.0),
                route("OPO", "LIS", 300.0),   // ciclo de volta à origem
                route("OPO", "MAD", 434.0));

        List<Itinerary> result = search.search(graph, "LIS", "MAD");

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getNumberOfStops());
    }
}
