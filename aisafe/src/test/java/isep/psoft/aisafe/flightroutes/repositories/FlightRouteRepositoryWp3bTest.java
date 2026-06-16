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
import isep.psoft.aisafe.flightroutes.domain.RouteUsage;
import isep.psoft.aisafe.flightroutes.domain.ScheduledFlight;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FlightRouteRepositoryWp3bTest {

    @Autowired private TestEntityManager em;
    @Autowired private FlightRouteRepository repository;

    private Airport persistAirport(String code) {
        Airport a = new Airport(new IATACode(code),
                new AirportDetails(code + " Airport", "City", "Portugal", "Europe", "Europe/Lisbon",
                        new Coordinates(40.0, -8.0)),
                AirportState.OPERATIONAL,
                List.of(new Runway("03/21", 3805.0, "030/210")));
        return em.persistAndFlush(a);
    }

    private FlightRoute persistRoute(Airport o, Airport d, double km, boolean active) {
        FlightRoute r = new FlightRoute(o, d, new RouteDistance(km),
                new RouteRequirements(1000.0, 100), new EstimatedFlightTime(60));
        if (!active) {
            r.changeStatus("INACTIVE");
        }
        return em.persistAndFlush(r);
    }

    private void persistFlight(String reg, FlightRoute route, LocalTime time) {
        em.persistAndFlush(new ScheduledFlight(reg, route,
                new FlightSchedule(LocalDate.of(2026, 7, 1), time)));
    }

    @Test
    void ensureSumActiveRoutesDistanceIsZeroWhenEmpty() {
        assertEquals(0.0, repository.sumActiveRoutesDistance());
    }

    @Test
    void ensureSumActiveRoutesDistanceCountsOnlyActive() {
        Airport lis = persistAirport("LIS");
        Airport opo = persistAirport("OPO");
        Airport mad = persistAirport("MAD");
        persistRoute(lis, opo, 300.0, true);
        persistRoute(opo, mad, 500.0, true);
        persistRoute(lis, mad, 999.0, false); // inativa — não conta

        assertEquals(800.0, repository.sumActiveRoutesDistance());
    }

    @Test
    void ensureFindAllActiveExcludesInactive() {
        Airport lis = persistAirport("LIS");
        Airport opo = persistAirport("OPO");
        persistRoute(lis, opo, 300.0, true);
        persistRoute(opo, lis, 300.0, false);

        List<FlightRoute> active = repository.findAllActive();

        assertEquals(1, active.size());
        assertEquals("ACTIVE", active.get(0).getStatus().getState());
    }

    @Test
    void ensureFindActiveRoutesWithUsageCountsScheduledFlightsAndExcludesInactive() {
        Airport lis = persistAirport("LIS");
        Airport opo = persistAirport("OPO");
        Airport mad = persistAirport("MAD");
        FlightRoute lisOpo = persistRoute(lis, opo, 300.0, true);  // 2 voos
        FlightRoute opoMad = persistRoute(opo, mad, 500.0, true);  // 0 voos
        persistRoute(lis, mad, 999.0, false);                      // inativa — excluída

        persistFlight("CS-AAA", lisOpo, LocalTime.of(8, 0));
        persistFlight("CS-BBB", lisOpo, LocalTime.of(9, 0));

        List<RouteUsage> usages = repository.findActiveRoutesWithUsage();

        // Só as 2 rotas ativas
        assertEquals(2, usages.size());
        Map<String, Long> byRoute = usages.stream()
                .collect(Collectors.toMap(u -> u.getRoute().getId(), RouteUsage::getUsageCount));
        assertEquals(2L, byRoute.get(lisOpo.getId()));  // LEFT JOIN conta 2
        assertEquals(0L, byRoute.get(opoMad.getId()));  // LEFT JOIN => 0 (sem voos)
    }
}
