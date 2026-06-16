package isep.psoft.aisafe.flightroutes.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItineraryTest {

    private FlightRoute legWithDistance(double km) {
        FlightRoute r = mock(FlightRoute.class, RETURNS_DEEP_STUBS);
        when(r.getDistance().getDistance()).thenReturn(km);
        return r;
    }

    @Test
    void ensureDirectRouteHasZeroStops() {
        Itinerary it = new Itinerary(List.of(legWithDistance(300.0)));

        assertEquals(0, it.getNumberOfStops());
        assertEquals(300.0, it.getTotalDistance());
        assertEquals(1, it.getLegs().size());
    }

    @Test
    void ensureMultiLegComputesStopsAndTotalDistance() {
        Itinerary it = new Itinerary(List.of(legWithDistance(300.0), legWithDistance(434.0)));

        assertEquals(1, it.getNumberOfStops());          // 2 legs => 1 stop
        assertEquals(734.0, it.getTotalDistance());
    }

    @Test
    void ensureNullLegsThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Itinerary(null));
    }

    @Test
    void ensureEmptyLegsThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Itinerary(List.of()));
    }
}
