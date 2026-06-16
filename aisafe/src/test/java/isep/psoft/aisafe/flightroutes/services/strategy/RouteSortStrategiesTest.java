package isep.psoft.aisafe.flightroutes.services.strategy;

import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
import isep.psoft.aisafe.flightroutes.domain.RouteUsage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RouteSortStrategiesTest {

    private RouteUsage usage(double distance, long count) {
        FlightRoute r = mock(FlightRoute.class, RETURNS_DEEP_STUBS);
        when(r.getDistance().getDistance()).thenReturn(distance);
        return new RouteUsage(r, count);
    }

    @Test
    void ensurePopularityKeyAndOrdering() {
        PopularitySortStrategy strategy = new PopularitySortStrategy();
        assertEquals("popularity", strategy.key());

        RouteUsage low = usage(300.0, 1);
        RouteUsage high = usage(300.0, 9);
        List<RouteUsage> sorted = strategy.sort(List.of(low, high));

        assertEquals(9L, sorted.get(0).getUsageCount());
        assertEquals(1L, sorted.get(1).getUsageCount());
    }

    @Test
    void ensureDistanceKeyAndOrdering() {
        DistanceSortStrategy strategy = new DistanceSortStrategy();
        assertEquals("distance", strategy.key());

        RouteUsage shortR = usage(200.0, 5);
        RouteUsage longR = usage(950.0, 5);
        List<RouteUsage> sorted = strategy.sort(List.of(shortR, longR));

        assertEquals(950.0, sorted.get(0).getRoute().getDistance().getDistance());
        assertEquals(200.0, sorted.get(1).getRoute().getDistance().getDistance());
    }
}
