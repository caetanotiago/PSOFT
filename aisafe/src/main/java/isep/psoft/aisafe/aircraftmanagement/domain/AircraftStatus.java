package isep.psoft.aisafe.aircraftmanagement.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class AircraftStatus {

    private String state;

    protected AircraftStatus() {}

    public AircraftStatus(String state) {
        if (state == null || state.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Invalid aircraft status. Must be AVAILABLE, IN_FLIGHT, UNDER_MAINTENANCE, or INACTIVE.");
        }
        String normalizedState = state.trim().toUpperCase();
        if (!normalizedState.equals("AVAILABLE") &&
            !normalizedState.equals("IN_FLIGHT") &&
            !normalizedState.equals("UNDER_MAINTENANCE") &&
            !normalizedState.equals("INACTIVE")) {
            throw new IllegalArgumentException(
                "Invalid aircraft status. Must be AVAILABLE, IN_FLIGHT, UNDER_MAINTENANCE, or INACTIVE.");
        }
        this.state = normalizedState;
    }

    public String getState() {
        return state;
    }
}