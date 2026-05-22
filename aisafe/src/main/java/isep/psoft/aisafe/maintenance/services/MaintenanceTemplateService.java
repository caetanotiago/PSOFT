package isep.psoft.aisafe.Maintenance.services;

import isep.psoft.aisafe.Maintenance.domain.MaintenanceInterval;
import isep.psoft.aisafe.Maintenance.domain.MaintenanceTemplate;
import isep.psoft.aisafe.Maintenance.domain.TemplateType;
import isep.psoft.aisafe.Maintenance.dto.CreateTemplateDTO;
import isep.psoft.aisafe.Maintenance.repositories.MaintenanceTemplateRepository;
import isep.psoft.aisafe.aircraft.domain.AircraftModel;
import isep.psoft.aisafe.aircraft.repositories.AircraftModelRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaintenanceTemplateService {

    private final MaintenanceTemplateRepository templateRepository;
    private final AircraftModelRepository aircraftModelRepository;

    // Injeção de dependências do Spring Boot
    public MaintenanceTemplateService(MaintenanceTemplateRepository templateRepository,
                                      AircraftModelRepository aircraftModelRepository) {
        this.templateRepository = templateRepository;
        this.aircraftModelRepository = aircraftModelRepository;
    }

    public MaintenanceTemplate createTemplate(CreateTemplateDTO dto) {
        // 1. Validar se o nome já existe
        Optional<MaintenanceTemplate> existing = templateRepository.findByTemplateName(dto.getTemplateName());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("A template with this name already exists.");
        }

        // 2. Converter os valores do DTO para os nossos Value Objects
        TemplateType type = TemplateType.valueOf(dto.getTemplateType().toUpperCase());
        MaintenanceInterval interval = new MaintenanceInterval(dto.getFlightHours(), dto.getCalendarDays());

        // 3. Ir buscar os Aircraft Models à base de dados usando os nomes fornecidos
        List<AircraftModel> models = aircraftModelRepository.findByDesignationIn(dto.getApplicableModels());

        if (models.isEmpty()) {
            throw new IllegalArgumentException("None of the provided Aircraft Models exist in the system.");
        }

        // 4. Criar a nova entidade e guardá-la
        MaintenanceTemplate newTemplate = new MaintenanceTemplate(
                dto.getTemplateName(),
                type,
                interval,
                dto.getChecklist(),
                models
        );

        return templateRepository.save(newTemplate);
    }
}