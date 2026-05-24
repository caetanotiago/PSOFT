package isep.psoft.aisafe.airports.domain;

public class AirportNotFoundException extends RuntimeException {

    public AirportNotFoundException(String iataCode) {
        super("Airport not found: " + iataCode);
    }
}
