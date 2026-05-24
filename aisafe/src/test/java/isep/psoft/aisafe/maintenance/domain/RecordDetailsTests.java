package isep.psoft.aisafe.maintenance.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

class RecordDetailsTests {

    @Test
    void shouldThrowExceptionWhenDescriptionIsNull() {

        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new RecordDetails(null, LocalDate.now(), 60);
        });
        assertThat(exception.getMessage()).isEqualTo("Description cannot be empty");
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsBlank() {

        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new RecordDetails("  ", LocalDate.now(), 60);
        });
        assertThat(exception.getMessage()).isEqualTo("Description cannot be empty");
    }

    @Test
    void shouldThrowExceptionWhenStartDateIsNull() {

        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new RecordDetails("Test Description", null, 60);
        });
        assertThat(exception.getMessage()).isEqualTo("Start date cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenDurationIsZero() {

        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new RecordDetails("Test Description", LocalDate.now(), 0);
        });
        assertThat(exception.getMessage()).isEqualTo("Expected duration must be greater than zero");
    }

    @Test
    void shouldThrowExceptionWhenDurationIsNegative() {

        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new RecordDetails("Test Description", LocalDate.now(), -10);
        });
        assertThat(exception.getMessage()).isEqualTo("Expected duration must be greater than zero");
    }

    @Test
    void shouldCreateRecordDetailsWithValidData() {

        String description = "Annual Inspection";
        LocalDate date = LocalDate.now();
        Integer duration = 120;

        RecordDetails details = new RecordDetails(description, date, duration);

        assertThat(details.getDescription()).isEqualTo(description);
        assertThat(details.getStartDate()).isEqualTo(date);
        assertThat(details.getExpectedDurationMinutes()).isEqualTo(duration);
    }
}
