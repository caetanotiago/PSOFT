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

        // Act
        RouteRequirements newReqs = new RouteRequirements(600.0, 200);
        EstimatedFlightTime newTime = new EstimatedFlightTime(75);
        route.updateDetails(newReqs, newTime);

        // Assert
        assertEquals(initialHistorySize + 1, route.getHistoryLog().size());
        assertEquals(200, route.getRequirements().getMinCapacity());
        assertEquals("Route requirements/time updated.", route.getHistoryLog().get(1).getDescription());
    }

    @Test
    void ensureDeactivatingRouteChangesStatusAndAddsHistory() {
        // Arrange
        Airport mockOrigin = mock(Airport.class);
        Airport mockDest = mock(Airport.class);
        FlightRoute route = new FlightRoute(mockOrigin, mockDest, new RouteDistance(300.0), 
                                            new RouteRequirements(500.0, 150), new EstimatedFlightTime(60));
        
        // Act
        route.deactivate();

        // Assert
        assertEquals("INACTIVE", route.getStatus().getState());
        assertEquals(2, route.getHistoryLog().size());
        assertEquals("Route deactivated.", route.getHistoryLog().get(1).getDescription());
    }
}