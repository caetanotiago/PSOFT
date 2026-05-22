package isep.psoft.aisafe.maintenance.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

class CompletionNotesTests {

    @Test
    void shouldThrowExceptionWhenNotesAreNull() {
        // Act & Assert
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new CompletionNotes(null, LocalDate.now());
        });
        assertThat(exception.getMessage()).isEqualTo("Completion notes cannot be empty.");
    }

    @Test
    void shouldThrowExceptionWhenNotesAreBlank() {
        // Act & Assert
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new CompletionNotes("  ", LocalDate.now());
        });
        assertThat(exception.getMessage()).isEqualTo("Completion notes cannot be empty.");
    }

    @Test
    void shouldThrowExceptionWhenCompletionDateIsNull() {
        // Act & Assert
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new CompletionNotes("All tasks completed.", null);
        });
        assertThat(exception.getMessage()).isEqualTo("Completion date cannot be null.");
    }

    @Test
    void shouldCreateCompletionNotesWithValidData() {
        // Arrange
        String notes = "All tasks completed successfully.";
        LocalDate date = LocalDate.now();

        // Act
        CompletionNotes completionNotes = new CompletionNotes(notes, date);

        // Assert
        assertThat(completionNotes.getNotes()).isEqualTo(notes);
        assertThat(completionNotes.getCompletionDate()).isEqualTo(date);
    }
}
