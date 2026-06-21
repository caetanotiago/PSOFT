package isep.psoft.aisafe.aircraftmanagement.domain;

/**
 * Lançada quando se tenta registar um AircraftModel com um modelName
 * que já existe (designation.modelName é único). Mapeada para 409 Conflict
 * pelo GlobalExceptionHandler.
 */
public class AircraftModelAlreadyExistsException extends RuntimeException {

    public AircraftModelAlreadyExistsException(String modelName) {
        super("Aircraft model '" + modelName + "' already exists.");
    }
}