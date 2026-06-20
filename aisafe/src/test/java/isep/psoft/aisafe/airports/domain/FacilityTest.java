package isep.psoft.aisafe.airports.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FacilityTest {

    @Test
    void valid_facility_is_accepted() {
        Facility facility = new Facility("TERMINAL", "Terminal 1", "Main terminal");
        assertEquals("TERMINAL", facility.getType());
        assertEquals("Terminal 1", facility.getIdentifier());
        assertEquals("Main terminal", facility.getDescription());
    }

    @Test
    void rejects_blank_type() {
        assertThrows(IllegalArgumentException.class, () -> new Facility(" ", "Terminal 1", null));
    }

    @Test
    void rejects_blank_identifier() {
        assertThrows(IllegalArgumentException.class, () -> new Facility("TERMINAL", "", null));
    }

    @Test
    void description_is_optional() {
        Facility facility = new Facility("GATE", "A12", null);
        assertNull(facility.getDescription());
    }

    @Test
    void equality_is_based_on_type_and_identifier() {
        Facility a = new Facility("GATE", "A12", "first description");
        Facility b = new Facility("GATE", "A12", "different description");
        assertEquals(a, b);
    }
}
