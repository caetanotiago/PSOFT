package isep.psoft.aisafe.aircraftmanagement.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AircraftStatusTest {
    @Test
    void ensureValidStatusIsAccepted() {
        assertDoesNotThrow(() -> new AircraftStatus("AVAILABLE"));
        assertEquals("AVAILABLE", new AircraftStatus("available").getState()); // Testa normalização
    }

    @Test
    void ensureInvalidStatusThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new AircraftStatus("INVALID"));
    }
}