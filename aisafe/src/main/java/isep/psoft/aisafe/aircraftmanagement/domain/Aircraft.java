package isep.psoft.aisafe.aircraftmanagement.domain;

import jakarta.persistence.*;

@Entity
public class Aircraft {

    @EmbeddedId
    private RegistrationNumber registrationNumber;

    @Embedded
    private AircraftStatus status;

    @Embedded
    private SeatingCapacity seatingCapacity;

    @Embedded
    private ManufacturingDate manufacturingDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "model_id")
    private AircraftModel model;

    @Version
    private Long version;

    protected Aircraft() {} // Obrigatório para o JPA

    public Aircraft(RegistrationNumber registrationNumber, AircraftModel model, 
                    ManufacturingDate manufacturingDate, SeatingCapacity seatingCapacity, 
                    AircraftStatus status) {
        if (registrationNumber == null || model == null || manufacturingDate == null || 
            seatingCapacity == null || status == null) {
            throw new IllegalArgumentException("Aircraft attributes cannot be null.");
        }
        this.registrationNumber = registrationNumber;
        this.model = model;
        this.manufacturingDate = manufacturingDate;
        this.seatingCapacity = seatingCapacity;
        this.status = status;
    }

    public RegistrationNumber getRegistrationNumber() { return registrationNumber; }

    public AircraftStatus getStatus() { return status; }

    public SeatingCapacity getSeatingCapacity() { return seatingCapacity; }

    public ManufacturingDate getManufacturingDate() { return manufacturingDate; }

    public AircraftModel getModel() { return model; }

    public Long getVersion() { return version; }

    // Regra de Negócio: Atualização do Status (para a US105)
    public void updateStatus(AircraftStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("New status cannot be null.");
        }
        this.status = newStatus;
    }
}