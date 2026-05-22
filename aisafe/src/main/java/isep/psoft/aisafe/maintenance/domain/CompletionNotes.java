package isep.psoft.aisafe.maintenance.domain;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class CompletionNotes {

    private String notes;
    private LocalDate completionDate;

    protected CompletionNotes() {} // JPA requirement

    public CompletionNotes(String notes, LocalDate completionDate) {
        if (notes == null || notes.trim().isEmpty()) {
            throw new IllegalArgumentException("Completion notes cannot be empty.");
        }
        if (completionDate == null) {
            throw new IllegalArgumentException("Completion date cannot be null.");
        }
        this.notes = notes;
        this.completionDate = completionDate;
    }

    public String getNotes() { return notes; }
    public LocalDate getCompletionDate() { return completionDate; }
}
