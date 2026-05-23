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

    protected AircraftModel() {} // Obrigatório para o JPA

    public AircraftModel(ModelDesignation designation, ModelSpecifications specifications) {
        if (designation == null || specifications == null) {
            throw new IllegalArgumentException("Designation and specifications cannot be null.");
        }
        this.designation = designation;
        this.specifications = specifications;
    }

    public Long getId() { return id; }

    public ModelDesignation getModelDesignation() { return designation; }

    public ModelSpecifications getSpecifications() { return specifications; }

    // Método utilitário adicionado por compatibilidade com `TemplateAssembler.java` 
    // que precisa de aceder diretamente à string do nome do modelo (se necessário):
    public String getDesignation() {
        return designation != null ? designation.getModelName() : null;
    }
}