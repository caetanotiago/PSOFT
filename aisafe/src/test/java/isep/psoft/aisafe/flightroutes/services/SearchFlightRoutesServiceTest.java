package isep.psoft.aisafe.flightroutes.services;

import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
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
class SearchFlightRoutesServiceTest {

    @Mock private FlightRouteRepository routeRepository;
    @InjectMocks private SearchFlightRoutesService service;

    @Test
    void ensureGetRouteByIdThrowsExceptionIfNotFound() {
        when(routeRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getRouteById("INVALID"));
    }

    @Test
    void ensureSearchByOriginAndDestinationCallsCorrectRepositoryMethod() {
        FlightRoute mockRoute = mock(FlightRoute.class);
        when(routeRepository.findByOriginAndDestination("LIS", "OPO")).thenReturn(List.of(mockRoute));

        List<FlightRoute> result = service.searchRoutes("LIS", "OPO");

        assertFalse(result.isEmpty());
        verify(routeRepository, times(1)).findByOriginAndDestination("LIS", "OPO");
    }
}