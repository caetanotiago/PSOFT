package isep.psoft.aisafe.maintenance.dto;

import java.util.List;

public class CreateTemplateDTO {
    private String templateName;
    private String templateType;
    private Integer flightHours;
    private Integer calendarDays;
    private List<String> checklist;
    private List<String> applicableModels;

    // Construtor vazio para o Jackson (JSON para Objeto)
    public CreateTemplateDTO() {
    }

    // Construtor completo para facilitar a criação em testes
    public CreateTemplateDTO(String templateName, String templateType, Integer flightHours, Integer calendarDays, List<String> checklist, List<String> applicableModels) {
        this.templateName = templateName;
        this.templateType = templateType;
        this.flightHours = flightHours;
        this.calendarDays = calendarDays;
        this.checklist = checklist;
        this.applicableModels = applicableModels;
    }

    // Getters
    public String getTemplateName() { return templateName; }
    public String getTemplateType() { return templateType; }
    public Integer getFlightHours() { return flightHours; }
    public Integer getCalendarDays() { return calendarDays; }
    public List<String> getChecklist() { return checklist; }
    public List<String> getApplicableModels() { return applicableModels; }
}
