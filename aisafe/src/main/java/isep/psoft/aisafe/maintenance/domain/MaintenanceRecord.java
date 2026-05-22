package isep.psoft.aisafe.maintenance.domain;

import jakarta.persistence.*;

@Entity
public class MaintenanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version // AC5: Impede modificações concorrentes (Optimistic Locking)
    private Long version;

    @Embedded
    private RecordDetails recordDetails;

    @Embedded
    private MaintenanceComponent component;

    @Embedded
    private CompletionNotes completionNotes; // Será null até ser concluído

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
        this.completionNotes = null; // Garante que começa como não concluído
    }

    // Lógica de negócio para concluir o registo
    public void complete(CompletionNotes notes) {
        if (this.completionNotes != null) {
            throw new IllegalStateException("This maintenance record has already been completed.");
        }
        this.completionNotes = notes;
    }

    // Getters
    public Long getId() { return id; }
    public Long getVersion() { return version; }
    public String getAircraftRegistration() { return aircraftRegistration; }
    public Long getMaintenanceTemplateId() { return maintenanceTemplateId; }
    public RecordDetails getRecordDetails() { return recordDetails; }
    public MaintenanceComponent getComponent() { return component; }
    public CompletionNotes getCompletionNotes() { return completionNotes; }
}
