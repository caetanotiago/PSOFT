package isep.psoft.aisafe.airports.domain;

/**
 * Thrown when an attempt is made to certify an aircraft model that is already
 * certified for a given airport. Belongs to the Airports bounded context because
 * the certification relationship (Airport.certifiedModels) is owned by the Airport
 * aggregate root.
 */
public class ModelAlreadyCertifiedException extends RuntimeException {

    public ModelAlreadyCertifiedException(String manufacturer, String modelName) {
        super("Aircraft model '" + manufacturer + " " + modelName + "' is already certified for this airport");
    }
}
