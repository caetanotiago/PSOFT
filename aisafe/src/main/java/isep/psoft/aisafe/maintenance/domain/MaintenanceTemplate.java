package isep.psoft.aisafe.maintenance.domain;

import isep.psoft.aisafe.aircraft.domain.AircraftModel; // Importa do mano que está a fazer os aviões
import jakarta.persistence.*;
import java.util.List;

@Entity
public class MaintenanceTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, unique = true)
    private String templateName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TemplateType templateType;

    @Embedded
    @Column(nullable = false)
    private MaintenanceInterval interval;

    @ElementCollection
    private List<String> checklist;

    @ManyToMany
    private List<AircraftModel> applicableModels;

    protected MaintenanceTemplate() {} // Para o JPA

    public MaintenanceTemplate(String templateName, TemplateType templateType,
                               MaintenanceInterval interval, List<String> checklist,
                               List<AircraftModel> applicableModels) {

        if (templateName == null || templateName.trim().isEmpty()) {
            throw new IllegalArgumentException("Template name cannot be empty");
        }
        if (applicableModels == null || applicableModels.isEmpty()) {
            throw new IllegalArgumentException("Template must apply to at least one Aircraft Model");
        }

        this.templateName = templateName;
        this.templateType = templateType;
        this.interval = interval;
        this.checklist = checklist;
        this.applicableModels = applicableModels;
    }

    // Getters
    public Long getId() { return id; }
    public String getTemplateName() { return templateName; }
    public TemplateType getTemplateType() { return templateType; }
    public MaintenanceInterval getInterval() { return interval; }
    public List<String> getChecklist() { return checklist; }
    public List<AircraftModel> getApplicableModels() { return applicableModels; }
}