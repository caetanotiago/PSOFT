package isep.psoft.aisafe.exceptions; // Pacote corrigido

import jakarta.persistence.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleBadRequest(RuntimeException ex) {
        Map<String, String> body = Map.of("error", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler({OptimisticLockException.class})
    protected ResponseEntity<Object> handleConflict(OptimisticLockException ex) {
        Map<String, String> body = Map.of("error", "The resource was modified by another user. Please refresh and try again.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
}
