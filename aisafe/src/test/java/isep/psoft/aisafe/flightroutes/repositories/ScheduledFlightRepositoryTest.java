package isep.psoft.aisafe.flightroutes.repositories;

import isep.psoft.aisafe.airports.domain.Airport;
import isep.psoft.aisafe.airports.domain.AirportDetails;
import isep.psoft.aisafe.airports.domain.AirportState;
import isep.psoft.aisafe.airports.domain.Coordinates;
import isep.psoft.aisafe.airports.domain.IATACode;
import isep.psoft.aisafe.airports.domain.Runway;
import isep.psoft.aisafe.flightroutes.domain.EstimatedFlightTime;
import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
import isep.psoft.aisafe.flightroutes.domain.FlightSchedule;
import isep.psoft.aisafe.flightroutes.domain.RouteDistance;
import isep.psoft.aisafe.flightroutes.domain.RouteRequirements;
import isep.psoft.aisafe.flightroutes.domain.ScheduledFlight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ScheduledFlightRepositoryTest {

    @Autowired private TestEntityManager em;
    @Autowired private ScheduledFlightRepository repository;

    private static final String REG = "CS-TVA";
    private static final LocalDate DATE = LocalDate.of(2026, 7, 1);
    private static final LocalTime TIME = LocalTime.of(10, 0);

    private FlightRoute route;

    @BeforeEach
    void setUp() {
        Airport lis = persistAirport("LIS", 38.77, -9.13);
        Airport opo = persistAirport("OPO", 41.24, -8.68);
        route = em.persistAndFlush(new FlightRoute(lis, opo,
                new RouteDistance(300.0), new RouteRequirements(1000.0, 100), new EstimatedFlightTime(60)));
        em.persistAndFlush(new ScheduledFlight(REG, route, new FlightSchedule(DATE, TIME)));
    }

    private Airport persistAirport(String code, double lat, double lon) {
        Airport a = new Airport(new IATACode(code),
                new AirportDetails(code + " Airport", "City", "Portugal", "Europe", "Europe/Lisbon",
                        new Coordinates(lat, lon)),
                AirportState.OPERATIONAL,
                List.of(new Runway("03/21", 3805.0, "030/210")));
        return em.persistAndFlush(a);
    }

    @Test
    void ensureOverlappingFlightDetectedForSameRegDateTime() {
        assertTrue(repository.existsOverlappingFlight(REG, DATE, TIME));
    }

    @Test
    void ensureNoOverlapForDifferentTime() {
        assertFalse(repository.existsOverlappingFlight(REG, DATE, LocalTime.of(15, 30)));
    }

    @Test
    void ensureNoOverlapForDifferentRegistration() {
        assertFalse(repository.existsOverlappingFlight("CS-XXX", DATE, TIME));
    }

    @Test
    void ensureFindByAircraftRegistrationReturnsOnlyThatAircraft() {
        em.persistAndFlush(new ScheduledFlight("CS-OTH", route, new FlightSchedule(DATE, LocalTime.of(12, 0))));

        Page<ScheduledFlight> page = repository.findByAircraftRegistration(REG, PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
        assertEquals(REG, page.getContent().get(0).getAircraftRegistration());
    }

    @Test
    void ensureFindByAircraftRegistrationReturnsEmptyForUnknown() {
        Page<ScheduledFlight> page = repository.findByAircraftRegistration("CS-NON", PageRequest.of(0, 10));
        assertTrue(page.isEmpty());
    }
}
