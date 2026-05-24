package isep.psoft.aisafe.airports.domain;

public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(AirportState from, AirportState to) {
        super("Cannot transition airport status from " + from + " to " + to);
    }
}
