package isep.psoft.aisafe.maintenance.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MaintenanceIntervalTests {

    @Test
    void ensureFlightHoursCannotBeNegative() {
        assertThrows(IllegalArgumentException.class, () -> new MaintenanceInterval(-100, 30));
    }

    @Test
    void ensureCalendarDaysCannotBeNegative() {
        assertThrows(IllegalArgumentException.class, () -> new MaintenanceInterval(100, -5));
    }

    @Test
    void ensureBothIntervalsCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> new MaintenanceInterval(null, null));
    }

    @Test
    void ensureCanCreateWithValidValues() {
        assertDoesNotThrow(() -> new MaintenanceInterval(100, 30));
        assertDoesNotThrow(() -> new MaintenanceInterval(100, null));
        assertDoesNotThrow(() -> new MaintenanceInterval(null, 30));
    }
}