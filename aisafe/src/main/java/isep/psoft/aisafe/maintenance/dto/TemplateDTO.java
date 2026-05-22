package isep.psoft.aisafe.Maintenance.dto;

import java.util.List;

public class TemplateDTO {
    private Long id;
    private String templateName;
    private String templateType;
    private Integer flightHours;
    private Integer calendarDays;
    private List<String> checklist;
    private List<String> applicableModels;

    public TemplateDTO(Long id, String templateName, String templateType, Integer flightHours, Integer calendarDays, List<String> checklist, List<String> applicableModels) {
        this.id = id;
        this.templateName = templateName;
        this.templateType = templateType;
        this.flightHours = flightHours;
        this.calendarDays = calendarDays;
        this.checklist = checklist;
        this.applicableModels = applicableModels;
    }

    // Getters
    public Long getId() { return id; }
    public String getTemplateName() { return templateName; }
    public String getTemplateType() { return templateType; }
    public Integer getFlightHours() { return flightHours; }
    public Integer getCalendarDays() { return calendarDays; }
    public List<String> getChecklist() { return checklist; }
    public List<String> getApplicableModels() { return applicableModels; }
}