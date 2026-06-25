package isep.psoft.aisafe.flightroutes.services;

import isep.psoft.aisafe.airports.domain.Airport;
import isep.psoft.aisafe.flightroutes.domain.EstimatedFlightTime;
import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
import isep.psoft.aisafe.flightroutes.domain.RouteDistance;
import isep.psoft.aisafe.flightroutes.domain.RouteHistory;
import isep.psoft.aisafe.flightroutes.domain.RouteRequirements;
import isep.psoft.aisafe.flightroutes.repositories.FlightRouteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetRouteHistoryServiceTest {

    @Mock private FlightRouteRepository routeRepository;
    @InjectMocks private GetRouteHistoryService service;

    @Test
    void ensureReturnsHistoryLogOfRoute() {
        FlightRoute route = new FlightRoute(mock(Airport.class), mock(Airport.class),
                new RouteDistance(500.0), new RouteRequirements(1000.0, 100), new EstimatedFlightTime(60));
        route.changeStatus("INACTIVE"); // adiciona 2ª entrada ao histórico
        when(routeRepository.findById("route-1")).thenReturn(Optional.of(route));

        List<RouteHistory> history = service.getRouteHistory("route-1");

        assertEquals(2, history.size());
        assertEquals("Route created.", history.get(0).getDescription());
        assertEquals("ACTIVE", history.get(0).getNewStatus()); // criação regista o estado inicial
        assertEquals("Route deactivated.", history.get(1).getDescription());
        assertEquals("ACTIVE", history.get(1).getPreviousStatus());
        assertEquals("INACTIVE", history.get(1).getNewStatus());
    }

    @Test
    void ensureThrowsNotFoundWhenRouteMissing() {
        when(routeRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getRouteHistory("missing"));
    }
}
