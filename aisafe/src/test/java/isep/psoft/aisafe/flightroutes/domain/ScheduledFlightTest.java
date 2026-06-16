package isep.psoft.aisafe.flightroutes.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ScheduledFlightTest {

    private final FlightRoute route = mock(FlightRoute.class);
    private final FlightSchedule schedule = mock(FlightSchedule.class);

    @Test
    void ensureValidScheduledFlightStartsInScheduledStatus() {
        ScheduledFlight flight = new ScheduledFlight("CS-TVA", route, schedule);

        assertEquals("CS-TVA", flight.getAircraftRegistration());
        assertSame(route, flight.getRoute());
        assertSame(schedule, flight.getSchedule());
        assertEquals("SCHEDULED", flight.getStatus().getState());
    }

    @Test
    void ensureNullRegistrationThrows() {
        assertThrows(IllegalArgumentException.class, () -> new ScheduledFlight(null, route, schedule));
    }

    @Test
    void ensureBlankRegistrationThrows() {
        assertThrows(IllegalArgumentException.class, () -> new ScheduledFlight("   ", route, schedule));
    }

    @Test
    void ensureNullRouteThrows() {
        assertThrows(IllegalArgumentException.class, () -> new ScheduledFlight("CS-TVA", null, schedule));
    }

    @Test
    void ensureNullScheduleThrows() {
        assertThrows(IllegalArgumentException.class, () -> new ScheduledFlight("CS-TVA", route, null));
    }
}
