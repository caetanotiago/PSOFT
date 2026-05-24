package isep.psoft.aisafe.airports.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinatesTest {

    @Test
    void accepts_boundary_values() {
        assertDoesNotThrow(() -> new Coordinates(-90.0, -180.0));
        assertDoesNotThrow(() -> new Coordinates(90.0, 180.0));
        assertDoesNotThrow(() -> new Coordinates(0.0, 0.0));
    }

    @Test
    void rejects_latitude_out_of_range() {
        assertThrows(IllegalArgumentException.class, () -> new Coordinates(-90.1, 0.0));
        assertThrows(IllegalArgumentException.class, () -> new Coordinates(90.1, 0.0));
    }

    @Test
    void rejects_longitude_out_of_range() {
        assertThrows(IllegalArgumentException.class, () -> new Coordinates(0.0, -180.1));
        assertThrows(IllegalArgumentException.class, () -> new Coordinates(0.0, 180.1));
    }

    @Test
    void rejects_null_values() {
        assertThrows(IllegalArgumentException.class, () -> new Coordinates(null, 0.0));
        assertThrows(IllegalArgumentException.class, () -> new Coordinates(0.0, null));
    }
}
