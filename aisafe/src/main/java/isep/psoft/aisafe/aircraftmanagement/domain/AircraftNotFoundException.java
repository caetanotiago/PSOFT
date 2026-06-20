package isep.psoft.aisafe.aircraftmanagement.domain;

public class AircraftNotFoundException extends RuntimeException {

    public AircraftNotFoundException(String registrationNumber) {
        super("Aircraft with registration number '" + registrationNumber + "' not found.");
    }
}