package isep.psoft.aisafe.flightroutes.services;

import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
import isep.psoft.aisafe.flightroutes.domain.RouteUsage;
import isep.psoft.aisafe.flightroutes.repositories.FlightRouteRepository;
import isep.psoft.aisafe.flightroutes.services.strategy.DistanceSortStrategy;
import isep.psoft.aisafe.flightroutes.services.strategy.PopularitySortStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // setup partilhado (distância/repo) nem sempre usado por caso
class ListActiveRoutesServiceTest {

    @Mock private FlightRouteRepository repository;

    private ListActiveRoutesService service;

    private RouteUsage shortPopular;  // distância 300, 5 usos
    private RouteUsage longUnpopular; // distância 900, 1 uso

    @BeforeEach
    void setUp() {
        // Estratégias reais (Strategy Pattern resolvido por key())
        service = new ListActiveRoutesService(repository,
                List.of(new PopularitySortStrategy(), new DistanceSortStrategy()));

        FlightRoute shortRoute = mock(FlightRoute.class, RETURNS_DEEP_STUBS);
        when(shortRoute.getDistance().getDistance()).thenReturn(300.0);
        FlightRoute longRoute = mock(FlightRoute.class, RETURNS_DEEP_STUBS);
        when(longRoute.getDistance().getDistance()).thenReturn(900.0);

        shortPopular = new RouteUsage(shortRoute, 5L);
        longUnpopular = new RouteUsage(longRoute, 1L);

        // Devolve sempre na ordem [shortPopular, longUnpopular] para a estratégia reordenar
        when(repository.findActiveRoutesWithUsage()).thenReturn(List.of(shortPopular, longUnpopular));
    }

    @Test
    void ensureSortByPopularityPutsMostUsedFirst() {
        List<RouteUsage> result = service.listActiveRoutes("popularity");

        assertEquals(5L, result.get(0).getUsageCount());
        assertEquals(1L, result.get(1).getUsageCount());
    }

    @Test
    void ensureSortByDistancePutsLongestFirst() {
        List<RouteUsage> result = service.listActiveRoutes("distance");

        assertEquals(900.0, result.get(0).getRoute().getDistance().getDistance());
        assertEquals(300.0, result.get(1).getRoute().getDistance().getDistance());
    }

    @Test
    void ensureNullDefaultsToPopularity() {
        List<RouteUsage> result = service.listActiveRoutes(null);
        assertEquals(5L, result.get(0).getUsageCount());
    }

    @Test
    void ensureBlankDefaultsToPopularity() {
        List<RouteUsage> result = service.listActiveRoutes("   ");
        assertEquals(5L, result.get(0).getUsageCount());
    }

    @Test
    void ensureSortByIsCaseInsensitive() {
        List<RouteUsage> result = service.listActiveRoutes("DISTANCE");
        assertEquals(900.0, result.get(0).getRoute().getDistance().getDistance());
    }

    @Test
    void ensureInvalidSortByThrowsBadRequest() {
        assertThrows(IllegalArgumentException.class, () -> service.listActiveRoutes("xpto"));
    }
}
