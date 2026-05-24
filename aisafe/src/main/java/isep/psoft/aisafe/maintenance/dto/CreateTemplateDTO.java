package isep.psoft.aisafe.maintenance.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CreateTemplateDTO {

    @NotBlank(message = "Template name is required")
    private String templateName;

    @NotBlank(message = "Template type is required")
    private String templateType;

    @Min(value = 1, message = "Flight hours must be positive if provided")
    private Integer flightHours;

    @Min(value = 1, message = "Calendar days must be positive if provided")
    private Integer calendarDays;

    @NotEmpty(message = "Checklist cannot be empty")
    private List<String> checklist;

    @NotEmpty(message = "At least one applicable model must be provided")
    private List<String> applicableModels;

    public CreateTemplateDTO() {}

    public CreateTemplateDTO(String templateName, String templateType, Integer flightHours, Integer calendarDays, List<String> checklist, List<String> applicableModels) {
        this.templateName = templateName;
        this.templateType = templateType;
        this.flightHours = flightHours;
        this.calendarDays = calendarDays;
        this.checklist = checklist;
        this.applicableModels = applicableModels;
    }

    public String getTemplateName() { return templateName; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }

    public String getTemplateType() { return templateType; }
    public void setTemplateType(String templateType) { this.templateType = templateType; }

    public Integer getFlightHours() { return flightHours; }
    public void setFlightHours(Integer flightHours) { this.flightHours = flightHours; }

    public Integer getCalendarDays() { return calendarDays; }
    public void setCalendarDays(Integer calendarDays) { this.calendarDays = calendarDays; }

    public List<String> getChecklist() { return checklist; }
    public void setChecklist(List<String> checklist) { this.checklist = checklist; }

    public List<String> getApplicableModels() { return applicableModels; }
    public void setApplicableModels(List<String> applicableModels) { this.applicableModels = applicableModels; }
}