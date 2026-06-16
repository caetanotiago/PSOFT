package isep.psoft.aisafe.flightroutes.services;

import isep.psoft.aisafe.flightroutes.dto.NetworkDistanceDTO;
import isep.psoft.aisafe.flightroutes.repositories.FlightRouteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculateNetworkDistanceServiceTest {

    @Mock private FlightRouteRepository repository;
    @InjectMocks private CalculateNetworkDistanceService service;

    @Test
    void ensureReturnsSumFromRepository() {
        when(repository.sumActiveRoutesDistance()).thenReturn(1012.5);

        NetworkDistanceDTO dto = service.calculateTotalDistance();

        assertEquals(1012.5, dto.getTotalDistanceKm());
    }

    @Test
    void ensureReturnsZeroWhenNoRoutes() {
        when(repository.sumActiveRoutesDistance()).thenReturn(0.0);

        assertEquals(0.0, service.calculateTotalDistance().getTotalDistanceKm());
    }
}
