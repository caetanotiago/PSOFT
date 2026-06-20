package isep.psoft.aisafe.maintenance.domain;

public class MaintenanceRecordNotFoundException extends RuntimeException {

    public MaintenanceRecordNotFoundException(Long id) {
        super("Maintenance record not found with ID: " + id);
    }
}