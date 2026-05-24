package isep.psoft.aisafe.airports.domain;

public class DuplicateIATACodeException extends RuntimeException {

    public DuplicateIATACodeException(String iataCode) {
        super("Airport with IATA code '" + iataCode + "' already exists");
    }
}
