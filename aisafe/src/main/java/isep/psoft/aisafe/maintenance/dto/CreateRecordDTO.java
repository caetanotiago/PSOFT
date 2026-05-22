package isep.psoft.aisafe.maintenance.dto;

import java.time.LocalDate;

public class CreateRecordDTO {
    private String aircraftRegistration;
    private Long templateId;
    private String description;
    private LocalDate startDate;
    private Integer expectedDurationMinutes;
    private String componentCategory;

    // Construtor vazio para o Jackson (JSON para Objeto)
    public CreateRecordDTO() {
    }

    // Construtor completo para facilitar a criação em testes
    public CreateRecordDTO(String aircraftRegistration, Long templateId, String description, LocalDate startDate, Integer expectedDurationMinutes, String componentCategory) {
        this.aircraftRegistration = aircraftRegistration;
        this.templateId = templateId;
        this.description = description;
        this.startDate = startDate;
        this.expectedDurationMinutes = expectedDurationMinutes;
        this.componentCategory = componentCategory;
    }

    // Getters
    public String getAircraftRegistration() { return aircraftRegistration; }
    public Long getTemplateId() { return templateId; }
    public String getDescription() { return description; }
    public LocalDate getStartDate() { return startDate; }
    public Integer getExpectedDurationMinutes() { return expectedDurationMinutes; }
    public String getComponentCategory() { return componentCategory; }
}
