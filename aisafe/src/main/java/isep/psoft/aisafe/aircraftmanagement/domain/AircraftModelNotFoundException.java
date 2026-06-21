package isep.psoft.aisafe.aircraftmanagement.domain;

public class AircraftModelNotFoundException extends RuntimeException {

    public AircraftModelNotFoundException(String modelName) {
        super("Aircraft model '" + modelName + "' not found.");
    }

    public AircraftModelNotFoundException(Long id) {
        super("Aircraft model with id '" + id + "' not found.");
    }
}