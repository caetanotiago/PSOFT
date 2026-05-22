package isep.psoft.aisafe.Maintenance.dto;

import java.util.List;

public class CreateTemplateDTO {
    private String templateName;
    private String templateType;
    private Integer flightHours;
    private Integer calendarDays;
    private List<String> checklist;
    private List<String> applicableModels;

    // Getters
    public String getTemplateName() { return templateName; }
    public String getTemplateType() { return templateType; }
    public Integer getFlightHours() { return flightHours; }
    public Integer getCalendarDays() { return calendarDays; }
    public List<String> getChecklist() { return checklist; }
    public List<String> getApplicableModels() { return applicableModels; }
}