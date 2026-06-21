package isep.psoft.aisafe.aircraftmanagement.domain;

/**
 * Lançada quando se tenta registar um Aircraft com um registrationNumber
 * que já existe. Mapeada para 409 Conflict pelo GlobalExceptionHandler.
 */
public class AircraftAlreadyExistsException extends RuntimeException {

    public AircraftAlreadyExistsException(String registrationNumber) {
        super("Aircraft with registration number '" + registrationNumber + "' already exists.");
    }
}