package isep.psoft.aisafe.flightroutes.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RouteRequirementsTest {

    @Test
    void ensureMinCapacityCannotBeNegative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new RouteRequirements(500.0, -10); // Alcance válido, capacidade inválida
        });
        assertEquals("Minimum capacity must be greater than zero.", exception.getMessage());
    }

    @Test
    void ensureMinRangeCannotBeNegative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new RouteRequirements(-100.0, 150); // Alcance inválido, capacidade válida
        });
        assertEquals("Minimum range must be greater than zero.", exception.getMessage());
    }

    @Test
    void ensureValidRequirementsAreCreatedSuccessfully() {
        RouteRequirements req = new RouteRequirements(1500.0, 200);
        assertEquals(1500.0, req.getMinRange());
        assertEquals(200, req.getMinCapacity());
    }
}