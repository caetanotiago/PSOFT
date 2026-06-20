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

    // US207: optional, structured facility/photo data — both collections may stay empty.
    @ElementCollection
    @CollectionTable(
            name = "airport_facilities",
            joinColumns = @JoinColumn(name = "airport_iata_code", referencedColumnName = "iata_code"))
    private List<Facility> facilities = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "airport_photos",
            joinColumns = @JoinColumn(name = "airport_iata_code", referencedColumnName = "iata_code"))
    private List<Photo> photos = new ArrayList<>();

    // US208: optional operating hours and contact list.
    @Embedded
    private OperatingHours operatingHours;

    @ElementCollection
    @CollectionTable(
            name = "airport_contacts",
            joinColumns = @JoinColumn(name = "airport_iata_code", referencedColumnName = "iata_code"))
    private List<AirportContact> contacts = new ArrayList<>();

    // @Version garante optimistic locking para US109 (PATCH status), reutilizado em US208.
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

    // US207: adds a Facility; rejects a duplicate (type, identifier) pair (Information Expert).
    public void addFacility(Facility facility) {
        if (facilities.contains(facility))
            throw new DuplicateFacilityException(facility.getType(), facility.getIdentifier());
        facilities.add(facility);
    }

    // US207: adds a Photo. No uniqueness invariant — an airport may have several photos.
    public void addPhoto(Photo photo) {
        photos.add(photo);
    }

    // US208: replaces the operating hours as a whole value.
    public void updateOperatingHours(OperatingHours newOperatingHours) {
        this.operatingHours = newOperatingHours;
    }

    // US208 / Conversation 014: replaces the entire contact list (full replace, not merge).
    public void updateContacts(List<AirportContact> newContacts) {
        this.contacts = new ArrayList<>(newContacts);
    }

    public IATACode getIataCode() { return iataCode; }
    public AirportDetails getDetails() { return details; }
    public AirportState getStatus() { return status; }
    public List<Runway> getRunways() { return List.copyOf(runways); }
    public Set<ModelDesignation> getCertifiedModels() { return Set.copyOf(certifiedModels); }
    public List<Facility> getFacilities() { return List.copyOf(facilities); }
    public List<Photo> getPhotos() { return List.copyOf(photos); }
    public OperatingHours getOperatingHours() { return operatingHours; }
    public List<AirportContact> getContacts() { return List.copyOf(contacts); }
    public Long getVersion() { return version; }
}
