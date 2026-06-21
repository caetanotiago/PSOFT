package isep.psoft.aisafe.maintenance.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

class CompletionNotesTests {

    @Test
    void shouldThrowExceptionWhenNotesAreNull() {
        // Act & Assert
        var exception = assertThrows(IllegalArgumentException.class, () -> new CompletionNotes(LocalDate.now(), null));

        assertThat(exception.getMessage()).isEqualTo("Completion notes cannot be null or blank.");
    }

    @Test
    void shouldThrowExceptionWhenNotesAreBlank() {
        // Act & Assert
        var exception = assertThrows(IllegalArgumentException.class, () -> new CompletionNotes(LocalDate.now(), "  "));

        assertThat(exception.getMessage()).isEqualTo("Completion notes cannot be null or blank.");
    }

    @Test
    void shouldThrowExceptionWhenCompletionDateIsNull() {
        // Act & Assert
        var exception = assertThrows(IllegalArgumentException.class, () -> new CompletionNotes(null, "All tasks completed."));

        assertThat(exception.getMessage()).isEqualTo("The completion date cannot be null.");
    }

    @Test
    void shouldCreateCompletionNotesWithValidData() {
        // Arrange
        String notes = "All tasks completed successfully.";
        LocalDate date = LocalDate.now();

        CompletionNotes completionNotes = new CompletionNotes(date, notes);

        // Assert
        assertThat(completionNotes.getNotes()).isEqualTo(notes);
        assertThat(completionNotes.getCompletionDate()).isEqualTo(date);
    }
}