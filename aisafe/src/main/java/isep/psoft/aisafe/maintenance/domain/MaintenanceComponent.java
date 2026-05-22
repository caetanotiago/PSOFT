package isep.psoft.aisafe.maintenance.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class MaintenanceComponent {

    private String category;

    protected MaintenanceComponent() {}

    public MaintenanceComponent(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Component category cannot be empty");
        }
        this.category = category;
    }

    public String getCategory() { return category; }
}