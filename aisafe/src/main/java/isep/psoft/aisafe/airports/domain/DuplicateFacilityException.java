package isep.psoft.aisafe.airports.domain;

// US207: thrown when a Facility with the same (type, identifier) pair is added twice to an Airport.
public class DuplicateFacilityException extends RuntimeException {

    public DuplicateFacilityException(String type, String identifier) {
        super("Facility of type '" + type + "' with identifier '" + identifier + "' is already registered for this airport");
    }
}