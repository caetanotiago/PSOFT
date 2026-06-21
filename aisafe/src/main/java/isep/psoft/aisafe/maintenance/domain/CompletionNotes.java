package isep.psoft.aisafe.maintenance.domain;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class CompletionNotes {

    // APAGADOS OS @Column(nullable = false) para a Base de Dados aceitar Manutenções "A Decorrer" (Nulas)
    private LocalDate completionDate;
    private String notes;
    private Double cost;
    private Integer actualDurationMinutes;
    private Double aircraftFlightHoursAtCompletion;

    protected CompletionNotes() {}

    // Construtores antigos (para manter a compatibilidade com a US119, 220 e 221)
    public CompletionNotes(LocalDate completionDate, String notes) {
        this(completionDate, notes, 0.0, 0, 0.0);
    }

    public CompletionNotes(LocalDate completionDate, String notes, Double cost) {
        this(completionDate, notes, cost, 0, 0.0);
    }

    public CompletionNotes(LocalDate completionDate, String notes, Double cost, Integer actualDurationMinutes) {
        this(completionDate, notes, cost, actualDurationMinutes, 0.0);
    }

    // Construtor principal
    public CompletionNotes(LocalDate completionDate, String notes, Double cost, Integer actualDurationMinutes, Double aircraftFlightHoursAtCompletion) {
        if (completionDate == null) throw new IllegalArgumentException("The completion date cannot be null.");
        if (notes == null || notes.trim().isEmpty()) throw new IllegalArgumentException("Completion notes cannot be null or blank.");
        if (cost == null || cost < 0) throw new IllegalArgumentException("Maintenance cost must be provided and cannot be negative.");
        if (actualDurationMinutes == null || actualDurationMinutes < 0) throw new IllegalArgumentException("Actual duration must be provided and cannot be negative.");
        if (aircraftFlightHoursAtCompletion == null || aircraftFlightHoursAtCompletion < 0) throw new IllegalArgumentException("Aircraft flight hours at completion must be provided and cannot be negative.");

        this.completionDate = completionDate;
        this.notes = notes;
        this.cost = cost;
        this.actualDurationMinutes = actualDurationMinutes;
        this.aircraftFlightHoursAtCompletion = aircraftFlightHoursAtCompletion;
    }

    public LocalDate getCompletionDate() { return completionDate; }
    public String getNotes() { return notes; }
    public Double getCost() { return cost; }
    public Integer getActualDurationMinutes() { return actualDurationMinutes; }
    public Double getAircraftFlightHoursAtCompletion() { return aircraftFlightHoursAtCompletion; }
}