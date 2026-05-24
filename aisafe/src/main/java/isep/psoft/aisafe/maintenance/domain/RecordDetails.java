package isep.psoft.aisafe.maintenance.domain;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class RecordDetails {

    private String description;
    private LocalDate startDate;
    private Integer expectedDurationMinutes;

    protected RecordDetails() {}

    public RecordDetails(String description, LocalDate startDate, Integer expectedDurationMinutes) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (expectedDurationMinutes == null || expectedDurationMinutes <= 0) {
            throw new IllegalArgumentException("Expected duration must be greater than zero");
        }

        this.description = description;
        this.startDate = startDate;
        this.expectedDurationMinutes = expectedDurationMinutes;
    }

    public String getDescription() { return description; }
    public LocalDate getStartDate() { return startDate; }
    public Integer getExpectedDurationMinutes() { return expectedDurationMinutes; }
}