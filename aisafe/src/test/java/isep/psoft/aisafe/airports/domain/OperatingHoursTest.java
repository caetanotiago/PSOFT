package isep.psoft.aisafe.airports.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class OperatingHoursTest {

    @Test
    void valid_hours_are_accepted() {
        OperatingHours hours = new OperatingHours(false, LocalTime.of(6, 0), LocalTime.of(23, 0));
        assertEquals(LocalTime.of(6, 0), hours.getOpens());
        assertEquals(LocalTime.of(23, 0), hours.getCloses());
        assertFalse(hours.isOperates24Hours());
    }

    @Test
    void rejects_opens_after_closes() {
        assertThrows(IllegalArgumentException.class, () ->
                new OperatingHours(false, LocalTime.of(23, 0), LocalTime.of(6, 0)));
    }

    @Test
    void rejects_opens_equal_to_closes() {
        assertThrows(IllegalArgumentException.class, () ->
                new OperatingHours(false, LocalTime.of(8, 0), LocalTime.of(8, 0)));
    }

    @Test
    void rejects_missing_times_when_not_24_hours() {
        assertThrows(IllegalArgumentException.class, () -> new OperatingHours(false, null, null));
    }

    @Test
    void operates_24_hours_does_not_require_open_close_times() {
        OperatingHours hours = new OperatingHours(true, null, null);
        assertTrue(hours.isOperates24Hours());
        assertNull(hours.getOpens());
        assertNull(hours.getCloses());
    }
}
