package isep.psoft.aisafe.aircraftmanagement.domain;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class ManufacturingDate {

    private LocalDate date;

    protected ManufacturingDate() {} // Obrigatório para o JPA

    public ManufacturingDate(LocalDate date) {
        if (date == null || date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Manufacturing date cannot be in the future.");
        }
        this.date = date;
    }

    public LocalDate getDate() { return date; }
}