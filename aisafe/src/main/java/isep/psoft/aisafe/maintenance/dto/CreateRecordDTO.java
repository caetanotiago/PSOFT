package isep.psoft.aisafe.maintenance.dto;

import java.time.LocalDate;

public class CreateRecordDTO {
    private String aircraftRegistration;
    private Long templateId;
    private String description;
    private LocalDate startDate;
    private Integer expectedDurationMinutes;
    private String componentCategory;

    // Getters
    public String getAircraftRegistration() { return aircraftRegistration; }
    public Long getTemplateId() { return templateId; }
    public String getDescription() { return description; }
    public LocalDate getStartDate() { return startDate; }
    public Integer getExpectedDurationMinutes() { return expectedDurationMinutes; }
    public String getComponentCategory() { return componentCategory; }
}