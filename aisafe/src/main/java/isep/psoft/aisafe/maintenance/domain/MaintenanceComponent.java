package isep.psoft.aisafe.maintenance.domain;

import jakarta.persistence.Embeddable;
import java.util.List;

@Embeddable
public class MaintenanceComponent {

    private String category;

    protected MaintenanceComponent() {}

    public MaintenanceComponent(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Component category cannot be empty.");
        }

        String normalized = category.trim().toUpperCase();
        List<String> validCategories = List.of("ENGINE", "AIRFRAME", "AVIONICS", "INTERIOR", "EXTERIOR");

        if (!validCategories.contains(normalized)) {
            throw new IllegalArgumentException("Invalid component category. Must be one of: ENGINE, AIRFRAME, AVIONICS, INTERIOR, EXTERIOR.");
        }

        this.category = normalized;
    }

    public String getCategory() {
        return category;
    }
}