package isep.psoft.aisafe.aircraftmanagement.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AircraftStatusTest {
    @Test
    void ensureValidStatusIsAccepted() {
        assertDoesNotThrow(() -> new AircraftStatus("ACTIVE"));
        assertEquals("ACTIVE", new AircraftStatus("active").getState()); // Testa normalização
    }

    @Test
    void ensureInvalidStatusThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new AircraftStatus("INVALID"));
    }
}