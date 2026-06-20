package isep.psoft.aisafe.maintenance.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class CompletionNotes {

    @Column(nullable = false)
    private LocalDate completionDate;

    private String notes;

    // NOVO: Adicionado para a US220
    @Column(nullable = false)
    private Double cost;

    protected CompletionNotes() {
        // Construtor vazio para o JPA
    }

    // Construtor antigo (mantido para não partir a US119 imediatamente)
    public CompletionNotes(LocalDate completionDate, String notes) {
        this(completionDate, notes, 0.0);
    }

    // NOVO: Construtor completo com custo
    public CompletionNotes(LocalDate completionDate, String notes, Double cost) {
        if (completionDate == null) {
            throw new IllegalArgumentException("The completion date cannot be null.");
        }
        if (cost == null || cost < 0) {
            throw new IllegalArgumentException("Maintenance cost must be provided and cannot be negative.");
        }
        this.completionDate = completionDate;
        this.notes = notes;
        this.cost = cost;
    }

    public LocalDate getCompletionDate() { return completionDate; }

    public String getNotes() { return notes; }

    public Double getCost() { return cost; }
}