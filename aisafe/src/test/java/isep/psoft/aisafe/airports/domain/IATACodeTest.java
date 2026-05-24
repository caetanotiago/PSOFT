package isep.psoft.aisafe.airports.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class IATACodeTest {

    @ParameterizedTest
    @ValueSource(strings = {"LIS", "OPO", "JFK", "LAX", "CDG"})
    void accepts_valid_codes(String code) {
        IATACode iata = new IATACode(code);
        assertEquals(code, iata.getCode());
    }

    @Test
    void rejects_null() {
        assertThrows(IllegalArgumentException.class, () -> new IATACode(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"li", "lis", "LI", "LISX", "L1S", "123", "", " LIS"})
    void rejects_invalid_formats(String code) {
        assertThrows(IllegalArgumentException.class, () -> new IATACode(code));
    }

    @Test
    void equality_is_value_based() {
        assertEquals(new IATACode("LIS"), new IATACode("LIS"));
        assertNotEquals(new IATACode("LIS"), new IATACode("OPO"));
    }

    @Test
    void toString_returns_code() {
        assertEquals("LIS", new IATACode("LIS").toString());
    }
}
