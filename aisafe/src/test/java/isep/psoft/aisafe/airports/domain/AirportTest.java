package isep.psoft.aisafe.airports.domain;

import isep.psoft.aisafe.domain.shared.ModelDesignation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AirportTest {

    private Airport airport;

    @BeforeEach
    void setUp() {
        airport = new Airport(
                new IATACode("LIS"),
                new AirportDetails("Humberto Delgado", "Lisbon", "Portugal", "Europe",
                        "Europe/Lisbon", new Coordinates(38.77, -9.13)),
                AirportState.OPERATIONAL,
                List.of(new Runway("03/21", 3805.0, "030/210"))
        );
    }

    @Test
    void constructor_rejects_invalid_iata() {
        assertThrows(IllegalArgumentException.class, () ->
                new Airport(new IATACode("li"), null, null, null));
    }

    @Test
    void airport_cannot_be_created_with_null_iata() {
        assertThrows(IllegalArgumentException.class, () ->
                new Airport(null,
                        new AirportDetails("X", "Y", "Z", null, "UTC", new Coordinates(0.0, 0.0)),
                        AirportState.OPERATIONAL,
                        List.of(new Runway("09/27", 2000.0, "090/270"))));
    }

    @Test
    void airport_cannot_be_created_with_null_details() {
        assertThrows(IllegalArgumentException.class, () ->
                new Airport(new IATACode("TST"), null, AirportState.OPERATIONAL,
                        List.of(new Runway("09/27", 2000.0, "090/270"))));
    }

    @Test
    void airport_cannot_be_created_with_empty_runways() {
        assertThrows(IllegalArgumentException.class, () ->
                new Airport(new IATACode("TST"),
                        new AirportDetails("X", "Y", "Z", null, "UTC", new Coordinates(0.0, 0.0)),
                        AirportState.OPERATIONAL,
                        List.of()));
    }

    @Test
    void constructor_defaults_to_operational_when_status_null() {
        Airport a = new Airport(new IATACode("OPO"),
                new AirportDetails("OPO", "Porto", "PT", "EU", "UTC", new Coordinates(41.0, -8.0)),
                null, List.of(new Runway("18/36", 3480.0, "180/360")));
        assertEquals(AirportState.OPERATIONAL, a.getStatus());
    }

    // ─── addCertification ────────────────────────────────────────────────────────

    @Test
    void addCertification_succeeds_for_new_model() {
        airport.addCertification(new ModelDesignation("Airbus", "A320"));
        assertEquals(1, airport.getCertifiedModels().size());
    }

    @Test
    void addCertification_throws_when_duplicate() {
        ModelDesignation md = new ModelDesignation("Airbus", "A320");
        airport.addCertification(md);
        assertThrows(ModelAlreadyCertifiedException.class, () -> airport.addCertification(md));
    }

    // ─── changeStatus ────────────────────────────────────────────────────────────

    @Test
    void changeStatus_valid_transition_operational_to_closed() {
        airport.changeStatus(AirportState.CLOSED);
        assertEquals(AirportState.CLOSED, airport.getStatus());
    }

    @Test
    void changeStatus_valid_transition_operational_to_under_maintenance() {
        airport.changeStatus(AirportState.UNDER_MAINTENANCE);
        assertEquals(AirportState.UNDER_MAINTENANCE, airport.getStatus());
    }

    @Test
    void changeStatus_invalid_transition_operational_to_operational() {
        assertThrows(InvalidStatusTransitionException.class,
                () -> airport.changeStatus(AirportState.OPERATIONAL));
    }

    @Test
    void changeStatus_under_maintenance_can_go_to_operational() {
        airport.changeStatus(AirportState.UNDER_MAINTENANCE);
        airport.changeStatus(AirportState.OPERATIONAL);
        assertEquals(AirportState.OPERATIONAL, airport.getStatus());
    }

    @Test
    void changeStatus_closed_cannot_stay_closed() {
        airport.changeStatus(AirportState.CLOSED);
        assertThrows(InvalidStatusTransitionException.class,
                () -> airport.changeStatus(AirportState.CLOSED));
    }
}
