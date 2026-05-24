package isep.psoft.aisafe.maintenance.dto;

import org.springframework.hateoas.RepresentationModel;
import java.util.List;

// 1. Adicionamos a extensão do RepresentationModel aqui:
public class TemplateDTO extends RepresentationModel<TemplateDTO> {

    private Long id;
    private String templateName;
    private String templateType;
    private Integer flightHours;
    private Integer calendarDays;
    private List<String> checklist;
    private List<String> applicableModels;

    public TemplateDTO() {} // Construtor vazio necessário para o Spring

    public TemplateDTO(Long id, String templateName, String templateType,
                       Integer flightHours, Integer calendarDays,
                       List<String> checklist, List<String> applicableModels) {
        this.id = id;
        this.templateName = templateName;
        this.templateType = templateType;
        this.flightHours = flightHours;
        this.calendarDays = calendarDays;
        this.checklist = checklist;
        this.applicableModels = applicableModels;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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