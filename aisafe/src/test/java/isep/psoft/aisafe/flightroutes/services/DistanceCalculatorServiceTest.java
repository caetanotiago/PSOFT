package isep.psoft.aisafe.flightroutes.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistanceCalculatorServiceTest {

    private final DistanceCalculatorService service = new DistanceCalculatorService();

    // Coordenadas reais (US110)
    private static final double LIS_LAT = 38.77, LIS_LON = -9.13;
    private static final double OPO_LAT = 41.24, OPO_LON = -8.68;

    @Test
    void ensureSamePointHasZeroDistance() {
        assertEquals(0.0, service.calculateDistance(LIS_LAT, LIS_LON, LIS_LAT, LIS_LON), 0.0001);
    }

    @Test
    void ensureKnownDistanceLisbonToPortoIsAboutRight() {
        double km = service.calculateDistance(LIS_LAT, LIS_LON, OPO_LAT, OPO_LON);
        // LIS↔OPO ≈ 275 km; tolerância ampla para acomodar arredondamentos
        assertEquals(275.0, km, 20.0);
    }

    @Test
    void ensureDistanceIsSymmetric() {
        double ab = service.calculateDistance(LIS_LAT, LIS_LON, OPO_LAT, OPO_LON);
        double ba = service.calculateDistance(OPO_LAT, OPO_LON, LIS_LAT, LIS_LON);
        assertEquals(ab, ba, 0.0001);
    }
}
