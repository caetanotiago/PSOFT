package isep.psoft.aisafe.maintenance.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

class RecordDetailsTests {

    @Test
    void shouldThrowExceptionWhenDescriptionIsNull() {
        // Act & Assert
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new RecordDetails(null, LocalDate.now(), 60);
        });
        assertThat(exception.getMessage()).isEqualTo("Description cannot be empty");
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsBlank() {
        // Act & Assert
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new RecordDetails("  ", LocalDate.now(), 60);
        });
        assertThat(exception.getMessage()).isEqualTo("Description cannot be empty");
    }

    @Test
    void shouldThrowExceptionWhenStartDateIsNull() {
        // Act & Assert
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new RecordDetails("Test Description", null, 60);
        });
        assertThat(exception.getMessage()).isEqualTo("Start date cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenDurationIsZero() {
        // Act & Assert
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new RecordDetails("Test Description", LocalDate.now(), 0);
        });
        assertThat(exception.getMessage()).isEqualTo("Expected duration must be greater than zero");
    }

    @Test
    void shouldThrowExceptionWhenDurationIsNegative() {
        // Act & Assert
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new RecordDetails("Test Description", LocalDate.now(), -10);
        });
        assertThat(exception.getMessage()).isEqualTo("Expected duration must be greater than zero");
    }

    @Test
    void shouldCreateRecordDetailsWithValidData() {
        // Arrange
        String description = "Annual Inspection";
        LocalDate date = LocalDate.now();
        Integer duration = 120;

        // Act
        RecordDetails details = new RecordDetails(description, date, duration);

        // Assert
        assertThat(details.getDescription()).isEqualTo(description);
        assertThat(details.getStartDate()).isEqualTo(date);
        assertThat(details.getExpectedDurationMinutes()).isEqualTo(duration);
    }
}
