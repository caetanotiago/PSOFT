package isep.psoft.aisafe.domain.shared;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ModelDesignation {

    @Column(name = "model_manufacturer")
    private String manufacturer;

    @Column(name = "model_name")
    private String modelName;

    protected ModelDesignation() {}

    public ModelDesignation(String manufacturer, String modelName) {
        this.manufacturer = manufacturer;
        this.modelName = modelName;
    }

    public String getManufacturer() { return manufacturer; }
    public String getModelName() { return modelName; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModelDesignation that)) return false;
        return Objects.equals(manufacturer, that.manufacturer)
                && Objects.equals(modelName, that.modelName);
    }

    @Override
    public int hashCode() { return Objects.hash(manufacturer, modelName); }
}
