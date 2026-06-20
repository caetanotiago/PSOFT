package isep.psoft.aisafe.maintenance.domain;

import jakarta.persistence.*;

@Entity
public class MaintenanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Embedded
    private RecordDetails recordDetails;

    @Embedded
    private MaintenanceComponent component;

    @Embedded
    private CompletionNotes completionNotes;

    @Column(nullable = false)
    private String aircraftRegistration;

    @Column(nullable = false)
    private Long maintenanceTemplateId;

    protected MaintenanceRecord() {}

    public MaintenanceRecord(String aircraftRegistration, Long maintenanceTemplateId,
                             RecordDetails recordDetails, MaintenanceComponent component) {
        this.aircraftRegistration = aircraftRegistration;
        this.maintenanceTemplateId = maintenanceTemplateId;
        this.recordDetails = recordDetails;
        this.component = component;
        this.completionNotes = null;
    }

    // Lógica de negócio para concluir o registo
    public void complete(CompletionNotes notes) {
        if (this.completionNotes != null) {
            throw new IllegalStateException("This maintenance record has already been completed.");
        }
        this.completionNotes = notes;
    }

    // --- NOVA LÓGICA PARA A US217 ---
    public void updateComponent(MaintenanceComponent newComponent) {
        if (newComponent == null) {
            throw new IllegalArgumentException("The maintenance component cannot be null.");
        }
        if (this.completionNotes != null) {
            throw new IllegalStateException("Cannot categorize a maintenance record that is already completed.");
        }
        this.component = newComponent;
    }

    public Long getId() { return id; }
    public Long getVersion() { return version; }
    public String getAircraftRegistration() { return aircraftRegistration; }
    public Long getMaintenanceTemplateId() { return maintenanceTemplateId; }
    public RecordDetails getRecordDetails() { return recordDetails; }
    public MaintenanceComponent getComponent() { return component; }
    public CompletionNotes getCompletionNotes() { return completionNotes; }
}