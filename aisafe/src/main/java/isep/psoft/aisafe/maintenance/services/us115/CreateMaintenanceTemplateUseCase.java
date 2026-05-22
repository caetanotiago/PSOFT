package isep.psoft.aisafe.maintenance.services.us115;

import isep.psoft.aisafe.aircraft.domain.AircraftModel;
import isep.psoft.aisafe.aircraft.repositories.AircraftModelRepository;
import isep.psoft.aisafe.maintenance.domain.MaintenanceInterval;
import isep.psoft.aisafe.maintenance.domain.MaintenanceTemplate;
import isep.psoft.aisafe.maintenance.domain.TemplateType;
import isep.psoft.aisafe.maintenance.dto.CreateTemplateDTO;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("CreateMaintenanceTemplateUseCase")
public class CreateMaintenanceTemplateUseCase {

    @Autowired
    private MaintenanceTemplateRepository templateRepository;
    @Autowired
    private AircraftModelRepository aircraftModelRepository;

    @Transactional
    public MaintenanceTemplate execute(CreateTemplateDTO dto) {
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
