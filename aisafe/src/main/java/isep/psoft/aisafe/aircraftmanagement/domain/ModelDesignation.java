package isep.psoft.aisafe.aircraftmanagement.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ModelDesignation {
    
    private String manufacturer;
    
    @Column(unique = true, nullable = false)
    private String modelName;

    protected ModelDesignation() {} // Obrigatório para o JPA

    public ModelDesignation(String manufacturer, String modelName) {
        if (manufacturer == null || !ManufacturerConfig.isValid(manufacturer)) {
            throw new IllegalArgumentException("Invalid manufacturer. Must be one of the predefined values.");
        }
        if (modelName == null || modelName.trim().isEmpty()) {
            throw new IllegalArgumentException("Model name cannot be null or empty.");
        }
        
        // Encontra o nome exato (com as maiúsculas corretas) para garantir consistência na BD
        for (ManufacturerConfig config : ManufacturerConfig.values()) {
            if (config.getManufacturerName().equalsIgnoreCase(manufacturer)) {
                this.manufacturer = config.getManufacturerName();
                break;
            }
        }
        this.modelName = modelName;
    }

    public String getManufacturer() { return manufacturer; }
    
    public String getModelName() { return modelName; }
}