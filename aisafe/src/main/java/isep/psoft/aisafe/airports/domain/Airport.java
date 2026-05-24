package isep.psoft.aisafe.airports.domain;

import isep.psoft.aisafe.domain.shared.ModelDesignation;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "airports")
public class Airport {

    // T1: @EmbeddedId — IATACode é VO persistido, não descartado após validação.
    // Slide "Value Objects in the Database" (7_REST_Web_API_DDD_Library).
    @EmbeddedId
    private IATACode iataCode;

    @Embedded
    private AirportDetails details;

    // T9 Option A: AirportStatus eliminado — campo único não justifica wrapper VO.
    // Se no futuro necessitar de "since" ou "changedBy", promover a VO.
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AirportState status;

    @ElementCollection
    @CollectionTable(
            name = "airport_runways",
            joinColumns = @JoinColumn(name = "airport_iata_code", referencedColumnName = "iata_code"))
    private List<Runway> runways = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "airport_certified_models",
            joinColumns = @JoinColumn(name = "airport_iata_code", referencedColumnName = "iata_code"))
    private Set<ModelDesignation> certifiedModels = new HashSet<>();

    // @Version garante optimistic locking para US109 (PATCH status).
    @Version
    private Long version;

    protected Airport() {}

    public Airport(IATACode iataCode, AirportDetails details, AirportState status, List<Runway> runways) {
        if (iataCode == null) throw new IllegalArgumentException("IATACode is required");
        if (details == null) throw new IllegalArgumentException("AirportDetails is required");
        if (runways == null || runways.isEmpty())
            throw new IllegalArgumentException("At least one runway is required");
        this.iataCode = iataCode;
        this.details = details;
        this.status = status != null ? status : AirportState.OPERATIONAL;
        this.runways = new ArrayList<>(runways);
    }

    // T7: exceção de domínio específica em vez de IllegalStateException genérico.
    public void addCertification(ModelDesignation modelDesignation) {
        if (certifiedModels.contains(modelDesignation))
            throw new ModelAlreadyCertifiedException(
                    modelDesignation.getManufacturer(), modelDesignation.getModelName());
        certifiedModels.add(modelDesignation);
    }

    // T5: transições de estado validadas pelo próprio domínio.
    public void changeStatus(AirportState newState) {
        if (!this.status.canTransitionTo(newState))
            throw new InvalidStatusTransitionException(this.status, newState);
        this.status = newState;
    }

    public IATACode getIataCode() { return iataCode; }
    public AirportDetails getDetails() { return details; }
    public AirportState getStatus() { return status; }
    public List<Runway> getRunways() { return List.copyOf(runways); }
    public Set<ModelDesignation> getCertifiedModels() { return Set.copyOf(certifiedModels); }
    public Long getVersion() { return version; }
}
