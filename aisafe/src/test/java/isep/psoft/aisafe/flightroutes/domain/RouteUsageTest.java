package isep.psoft.aisafe.flightroutes.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RouteUsageTest {

    @Test
    void ensureUsageCountIsPreserved() {
        RouteUsage usage = new RouteUsage(mock(FlightRoute.class), 7L);
        assertEquals(7L, usage.getUsageCount());
    }

    @Test
    void ensureNullUsageCountDefaultsToZero() {
        RouteUsage usage = new RouteUsage(mock(FlightRoute.class), null);
        assertEquals(0L, usage.getUsageCount());
    }
}
