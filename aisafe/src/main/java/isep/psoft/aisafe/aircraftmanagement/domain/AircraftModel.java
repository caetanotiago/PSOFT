package isep.psoft.aisafe.aircraftmanagement.domain;

import jakarta.persistence.*;

@Entity
public class AircraftModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ModelDesignation designation;

    @Embedded
    private ModelSpecifications specifications;

    @Embedded
    private ModelImage image; // US202 — imagem opcional

    @Version
    private Long version; // US201 — Optimistic Locking

    protected AircraftModel() {}

    public AircraftModel(ModelDesignation designation, ModelSpecifications specifications) {
        if (designation == null || specifications == null) {
            throw new IllegalArgumentException("Designation and specifications cannot be null.");
        }
        this.designation = designation;
        this.specifications = specifications;
    }

    public AircraftModel(ModelDesignation designation, ModelSpecifications specifications, ModelImage image) {
        this(designation, specifications);
        this.image = image; // pode ser null (opcional)
    }

    public Long getId() { return id; }
    public ModelDesignation getModelDesignation() { return designation; }
    public ModelSpecifications getSpecifications() { return specifications; }
    public ModelImage getImage() { return image; }
    public Long getVersion() { return version; }

    // US201 — atualiza as specs (regra de negócio na entidade)
    public void updateSpecifications(ModelSpecifications newSpecifications) {
        if (newSpecifications == null) {
            throw new IllegalArgumentException("New specifications cannot be null.");
        }
        this.specifications = newSpecifications;
    }
}