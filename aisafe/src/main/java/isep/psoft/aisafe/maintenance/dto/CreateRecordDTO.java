package isep.psoft.aisafe.maintenance.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class CreateRecordDTO {

    @NotBlank(message = "Aircraft registration cannot be blank")
    private String aircraftRegistration;

    @NotNull(message = "Template ID cannot be null")
    private Long templateId;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Start date cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "Expected duration cannot be null")
    @Min(value = 1, message = "Expected duration must be at least 1 minute")
    private Integer expectedDurationMinutes;

    private String componentCategory;

    public CreateRecordDTO() {}

    public CreateRecordDTO(String aircraftRegistration, Long templateId, String description, LocalDate startDate, Integer expectedDurationMinutes, String componentCategory) {
        this.aircraftRegistration = aircraftRegistration;
        this.templateId = templateId;
        this.description = description;
        this.startDate = startDate;
        this.expectedDurationMinutes = expectedDurationMinutes;
        this.componentCategory = componentCategory;
    }

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
}