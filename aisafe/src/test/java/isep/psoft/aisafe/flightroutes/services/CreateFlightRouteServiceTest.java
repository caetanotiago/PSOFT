package isep.psoft.aisafe.flightroutes.services;

import isep.psoft.aisafe.airports.domain.IATACode;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import isep.psoft.aisafe.flightroutes.dto.CreateRouteDTO;
import isep.psoft.aisafe.flightroutes.factories.FlightRouteFactory;
import isep.psoft.aisafe.flightroutes.repositories.FlightRouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateFlightRouteServiceTest {

    @Mock private AirportRepository airportRepository;
    @Mock private FlightRouteRepository routeRepository;
    @Mock private FlightRouteFactory routeFactory;
    @Mock private DistanceCalculatorService distanceCalculator;

    @InjectMocks private CreateFlightRouteService service;

    private CreateRouteDTO dto;

    @BeforeEach
    void setUp() {
        dto = new CreateRouteDTO();
        dto.setOriginIATA("LIS");
        dto.setDestIATA("OPO");
        dto.setMinRange(500.0);
        dto.setMinCapacity(150);
        dto.setEstimatedFlightTime(50);
    }

    @Test
    void ensureThrowsExceptionIfRouteAlreadyExists() {
        // Simula que o repositório diz que a rota já existe
        when(routeRepository.existsByOriginAndDestination("LIS", "OPO")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.createRoute(dto));
        
        // Garante que o processo parou aqui e não tentou gravar nada
        verify(routeRepository, never()).save(any());
    }

    @Test
    void ensureThrowsExceptionIfOriginAirportNotFound() {
        when(routeRepository.existsByOriginAndDestination("LIS", "OPO")).thenReturn(false);
        // Simula que o aeroporto de origem não existe na BD
        when(airportRepository.findById(new IATACode("LIS"))).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.createRoute(dto));
    }
}