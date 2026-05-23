package isep.psoft.aisafe.aircraftmanagement.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class AircraftStatus {

    private String state;

    protected AircraftStatus() {} // Obrigatório para o JPA

    public AircraftStatus(String state) {
        if (state == null || state.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid aircraft status. Must be ACTIVE, INACTIVE, or UNDER_MAINTENANCE.");
        }
        
        String normalizedState = state.trim().toUpperCase();
        if (!normalizedState.equals("ACTIVE") && !normalizedState.equals("INACTIVE") && !normalizedState.equals("UNDER_MAINTENANCE")) {
            throw new IllegalArgumentException("Invalid aircraft status. Must be ACTIVE, INACTIVE, or UNDER_MAINTENANCE.");
        }
        this.state = normalizedState;
    }

    public String getState() {
        return state;
    }
}