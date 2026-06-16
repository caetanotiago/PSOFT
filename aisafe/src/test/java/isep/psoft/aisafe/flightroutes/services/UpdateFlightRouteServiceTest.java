package isep.psoft.aisafe.flightroutes.services;

import isep.psoft.aisafe.airports.domain.Airport;
import isep.psoft.aisafe.flightroutes.domain.EstimatedFlightTime;
import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
import isep.psoft.aisafe.flightroutes.domain.RouteDistance;
import isep.psoft.aisafe.flightroutes.domain.RouteRequirements;
import isep.psoft.aisafe.flightroutes.dto.UpdateRouteDTO;
import isep.psoft.aisafe.flightroutes.repositories.FlightRouteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateFlightRouteServiceTest {

    @Mock private FlightRouteRepository routeRepository;
    @InjectMocks private UpdateFlightRouteService service;

    private FlightRoute route;
    private static final String ID = "route-1";

    @BeforeEach
    void setUp() {
        // Rota real: minRange=1000, minCapacity=100, time=60, status=ACTIVE
        route = new FlightRoute(mock(Airport.class), mock(Airport.class),
                new RouteDistance(500.0), new RouteRequirements(1000.0, 100), new EstimatedFlightTime(60));
    }

    private UpdateRouteDTO dto(Double minRange, Integer minCapacity, Integer time, String status) {
        UpdateRouteDTO d = new UpdateRouteDTO();
        d.setMinRange(minRange);
        d.setMinCapacity(minCapacity);
        d.setEstimatedFlightTime(time);
        d.setStatus(status);
        return d;
    }

    private void stubFound() {
        when(routeRepository.findById(ID)).thenReturn(Optional.of(route));
        when(routeRepository.save(any(FlightRoute.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void ensurePartialUpdateOfOnlyCapacityKeepsOtherFields() {
        stubFound();

        FlightRoute result = service.updateRoute(ID, dto(null, 180, null, null));

        assertEquals(180, result.getRequirements().getMinCapacity());
        assertEquals(1000.0, result.getRequirements().getMinRange());      // inalterado
        assertEquals(60, result.getEstimatedFlightTime().getDurationMinutes()); // inalterado
    }

    @Test
    void ensurePartialUpdateOfOnlyFlightTimeWorks() {
        stubFound();

        FlightRoute result = service.updateRoute(ID, dto(null, null, 99, null));

        assertEquals(99, result.getEstimatedFlightTime().getDurationMinutes());
        assertEquals(100, result.getRequirements().getMinCapacity()); // inalterado
    }

    @Test
    void ensureDeactivateThenReactivateWorks() {
        stubFound();

        FlightRoute deactivated = service.updateRoute(ID, dto(null, null, null, "INACTIVE"));
        assertEquals("INACTIVE", deactivated.getStatus().getState());

        FlightRoute reactivated = service.updateRoute(ID, dto(null, null, null, "ACTIVE"));
        assertEquals("ACTIVE", reactivated.getStatus().getState());
    }

    @Test
    void ensureStatusAndDetailsCanBeUpdatedTogether() {
        stubFound();

        FlightRoute result = service.updateRoute(ID, dto(7777.0, null, null, "INACTIVE"));

        assertEquals("INACTIVE", result.getStatus().getState());
        assertEquals(7777.0, result.getRequirements().getMinRange());
    }

    @Test
    void ensureChangingToSameStatusThrowsConflict() {
        when(routeRepository.findById(ID)).thenReturn(Optional.of(route)); // rota já ACTIVE

        assertThrows(IllegalStateException.class,
                () -> service.updateRoute(ID, dto(null, null, null, "ACTIVE")));
    }

    @Test
    void ensureInvalidStatusThrowsBadRequest() {
        when(routeRepository.findById(ID)).thenReturn(Optional.of(route));

        assertThrows(IllegalArgumentException.class,
                () -> service.updateRoute(ID, dto(null, null, null, "FOO")));
    }

    @Test
    void ensureNotFoundThrowsAndDoesNotSave() {
        when(routeRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.updateRoute("missing", dto(null, 180, null, null)));
        verify(routeRepository, never()).save(any());
    }
}
