package isep.psoft.aisafe.maintenance.dto;

import org.springframework.hateoas.RepresentationModel;
import java.time.LocalDate;

// 1. A classe agora estende RepresentationModel!
public class MaintenanceRecordOutputDto extends RepresentationModel<MaintenanceRecordOutputDto> {

    private Long id;
    private String aircraftRegistration;
    private Long templateId;
    private String description;
    private LocalDate startDate;
    private Integer expectedDurationMinutes;
    private String componentCategory;
    private String completionNotes; // Pode ser null se não estiver concluído

    public MaintenanceRecordOutputDto() {}

    // Getters e Setters normais
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAircraftRegistration() { return aircraftRegistration; }
    public void setAircraftRegistration(String aircraftRegistration) { this.aircraftRegistration = aircraftRegistration; }

    public Long getTemplateId() { return templateId; }
    public void setTemplateId(Long templateId) { this.templateId = templateId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public Integer getExpectedDurationMinutes() { return expectedDurationMinutes; }
    public void setExpectedDurationMinutes(Integer expectedDurationMinutes) { this.expectedDurationMinutes = expectedDurationMinutes; }

    public String getComponentCategory() { return componentCategory; }
    public void setComponentCategory(String componentCategory) { this.componentCategory = componentCategory; }

    public String getCompletionNotes() { return completionNotes; }
    public void setCompletionNotes(String completionNotes) { this.completionNotes = completionNotes; }
}