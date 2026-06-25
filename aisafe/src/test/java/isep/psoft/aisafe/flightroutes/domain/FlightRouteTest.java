package isep.psoft.aisafe.flightroutes.domain;

import isep.psoft.aisafe.airports.domain.Airport;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class FlightRouteTest {

    @Test
    void ensureUpdatingRouteAddsHistoryRecord() {
        // Arrange
        Airport mockOrigin = mock(Airport.class);
        Airport mockDest = mock(Airport.class);
        RouteDistance distance = new RouteDistance(300.0);
        
        // AQUI ESTÁ A CORREÇÃO: Usar RouteRequirements e não RouteRequirementsTest
        RouteRequirements initialReqs = new RouteRequirements(500.0, 150);
        EstimatedFlightTime initialTime = new EstimatedFlightTime(60);
        
        FlightRoute route = new FlightRoute(mockOrigin, mockDest, distance, initialReqs, initialTime);
        int initialHistorySize = route.getHistoryLog().size();
        assertEquals(1, initialHistorySize);

        // Act — atualização parcial (só capacidade e tempo); minRange fica inalterado.
        route.updateDetails(null, 200, 75);

        // Assert
        assertEquals(initialHistorySize + 1, route.getHistoryLog().size());
        assertEquals(200, route.getRequirements().getMinCapacity());
        assertEquals(500.0, route.getRequirements().getMinRange());
        RouteHistory record = route.getHistoryLog().get(1);
        assertEquals("Route details updated.", record.getDescription());
        // Histórico dinâmico: só os atributos alterados têm "previous"/"new"; minRange não mudou.
        assertNull(record.getPreviousMinRange());
        assertEquals(150, record.getPreviousMinCapacity());
        assertEquals(60, record.getPreviousEstimatedFlightTime());
        // Valores novos (para que mudou) — também só nos atributos alterados.
        assertNull(record.getNewMinRange());
        assertEquals(200, record.getNewMinCapacity());
        assertEquals(75, record.getNewEstimatedFlightTime());
    }

    @Test
    void ensureDeactivatingRouteChangesStatusAndAddsHistory() {
        // Arrange
        Airport mockOrigin = mock(Airport.class);
        Airport mockDest = mock(Airport.class);
        FlightRoute route = new FlightRoute(mockOrigin, mockDest, new RouteDistance(300.0), 
                                            new RouteRequirements(500.0, 150), new EstimatedFlightTime(60));
        
        // Act
        route.changeStatus("INACTIVE");

        // Assert
        assertEquals("INACTIVE", route.getStatus().getState());
        assertEquals(2, route.getHistoryLog().size());
        RouteHistory record = route.getHistoryLog().get(1);
        assertEquals("Route deactivated.", record.getDescription());
        assertEquals("ACTIVE", record.getPreviousStatus());
        assertEquals("INACTIVE", record.getNewStatus());
    }

    @Test
    void ensureCreationRecordsInitialValuesAsNew() {
        Airport mockOrigin = mock(Airport.class);
        Airport mockDest = mock(Airport.class);
        FlightRoute route = new FlightRoute(mockOrigin, mockDest, new RouteDistance(300.0),
                                            new RouteRequirements(500.0, 150), new EstimatedFlightTime(60));

        RouteHistory created = route.getHistoryLog().get(0);
        assertEquals("Route created.", created.getDescription());
        // Criação não tem "previous", mas regista os valores iniciais como "new".
        assertNull(created.getPreviousStatus());
        assertEquals(500.0, created.getNewMinRange());
        assertEquals(150, created.getNewMinCapacity());
        assertEquals(60, created.getNewEstimatedFlightTime());
        assertEquals("ACTIVE", created.getNewStatus());
    }

    @Test
    void ensureReactivatingRouteWorks() {
        Airport mockOrigin = mock(Airport.class);
        Airport mockDest = mock(Airport.class);
        FlightRoute route = new FlightRoute(mockOrigin, mockDest, new RouteDistance(300.0),
                                            new RouteRequirements(500.0, 150), new EstimatedFlightTime(60));
        route.changeStatus("INACTIVE");

        // Act — reativar
        route.changeStatus("ACTIVE");

        // Assert
        assertEquals("ACTIVE", route.getStatus().getState());
        assertEquals("Route activated.", route.getHistoryLog().get(2).getDescription());
        assertEquals("INACTIVE", route.getHistoryLog().get(2).getPreviousStatus());
        assertEquals("ACTIVE", route.getHistoryLog().get(2).getNewStatus());
    }

    @Test
    void ensureChangingToSameStatusThrows() {
        Airport mockOrigin = mock(Airport.class);
        Airport mockDest = mock(Airport.class);
        FlightRoute route = new FlightRoute(mockOrigin, mockDest, new RouteDistance(300.0),
                                            new RouteRequirements(500.0, 150), new EstimatedFlightTime(60));

        // Rota nasce ACTIVE → tentar ativar de novo deve falhar.
        assertThrows(IllegalStateException.class, () -> route.changeStatus("ACTIVE"));
    }
}