package isep.psoft.aisafe.aircraftmanagement.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class SeatingCapacity {

    private Integer totalSeats;

    protected SeatingCapacity() {} // Obrigatório para o JPA

    public SeatingCapacity(Integer totalSeats) {
        if (totalSeats == null || totalSeats <= 0) {
            throw new IllegalArgumentException("Instance seating capacity must be strictly positive.");
        }
        this.totalSeats = totalSeats;
    }

    public Integer getTotalSeats() { return totalSeats; }
}