package isep.psoft.aisafe.exceptions;

import isep.psoft.aisafe.airports.domain.AirportNotFoundException;
import isep.psoft.aisafe.airports.domain.DuplicateIATACodeException;
import isep.psoft.aisafe.airports.domain.InvalidStatusTransitionException;
import isep.psoft.aisafe.airports.domain.ModelAlreadyCertifiedException;
import isep.psoft.aisafe.flightroutes.domain.RouteRequirementsNotMetException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Invalid input (e.g. invalid IATA format, unknown state string) → 400 Bad Request
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Airport not found by IATA code → 404 Not Found
    @ExceptionHandler(AirportNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAirportNotFound(AirportNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Duplicate IATA code on registration → 409 Conflict
    @ExceptionHandler(DuplicateIATACodeException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateIATACode(DuplicateIATACodeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Invalid or same-state status transition → 409 Conflict
    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<Map<String, String>> handleInvalidStatusTransition(InvalidStatusTransitionException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Aircraft model already certified for this airport → 409 Conflict
    @ExceptionHandler(ModelAlreadyCertifiedException.class)
    public ResponseEntity<Map<String, String>> handleModelAlreadyCertified(ModelAlreadyCertifiedException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Concurrent modification detected via @Version (optimistic locking) → 409 Conflict
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Map<String, String>> handleOptimisticLocking(ObjectOptimisticLockingFailureException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "The resource was updated by another user. Please refresh and try again.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Bean Validation failures (@Valid on request body/params) → 400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // --- EXCEÇÕES DA MANUTENÇÃO (WP#4A) ---

    // Registo de manutenção já se encontra concluído → 409 Conflict
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Apanha a exceção direta de concorrência do JPA lançada na US119 → 409 Conflict
    @ExceptionHandler(jakarta.persistence.OptimisticLockException.class)
    public ResponseEntity<Map<String, String>> handleJakartaOptimisticLocking(jakarta.persistence.OptimisticLockException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // --- EXCEÇÕES DO WP#3B (Flight Operations) ---

    // Aeronave não cumpre os requisitos da rota (alcance/capacidade) → 422 Unprocessable Entity
    @ExceptionHandler(RouteRequirementsNotMetException.class)
    public ResponseEntity<Map<String, String>> handleRouteRequirementsNotMet(RouteRequirementsNotMetException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    @ExceptionHandler(isep.psoft.aisafe.maintenance.domain.MaintenanceRecordNotFoundException.class)
    public ResponseEntity<String> handleMaintenanceRecordNotFound(isep.psoft.aisafe.maintenance.domain.MaintenanceRecordNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}