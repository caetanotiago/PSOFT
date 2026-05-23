package isep.psoft.aisafe.maintenance.services.us115; // Ajustado para a pasta correta da tua estrutura

import isep.psoft.aisafe.aircraftmanagement.domain.AircraftModel;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftModelRepository;
import isep.psoft.aisafe.maintenance.domain.MaintenanceInterval;
import isep.psoft.aisafe.maintenance.domain.MaintenanceTemplate;
import isep.psoft.aisafe.maintenance.domain.TemplateType;
import isep.psoft.aisafe.maintenance.dto.CreateTemplateDTO;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CreateMaintenanceTemplateUseCase { // 1. O nome da classe mudou aqui

    private final MaintenanceTemplateRepository templateRepository;
    private final AircraftModelRepository aircraftModelRepository;

    // 2. O nome do construtor tem de ser igual ao nome da classe
    public CreateMaintenanceTemplateUseCase(MaintenanceTemplateRepository templateRepository,
                                            AircraftModelRepository aircraftModelRepository) {
        this.templateRepository = templateRepository;
        this.aircraftModelRepository = aircraftModelRepository;
    }

    // 3. O método chama-se "execute" para bater certo com o teu Controller
    public MaintenanceTemplate execute(CreateTemplateDTO dto) {
        // 1. Validar se o nome já existe
        Optional<MaintenanceTemplate> existing = templateRepository.findByTemplateName(dto.getTemplateName());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("A template with this name already exists.");
        }

        // 2. Converter os valores do DTO
        TemplateType type = TemplateType.valueOf(dto.getTemplateType().toUpperCase());
        MaintenanceInterval interval = new MaintenanceInterval(dto.getFlightHours(), dto.getCalendarDays());

        // 3. Ir buscar os Aircraft Models usando o método do repositório do teu colega
        List<AircraftModel> models = new ArrayList<>();

        for (String modelName : dto.getApplicableModels()) {
            // Usa o método findByDesignationModelName que o teu colega criou
            Optional<AircraftModel> foundModel = aircraftModelRepository.findByDesignationModelName(modelName);

            // Se encontrar, adiciona à nossa lista
            foundModel.ifPresent(models::add);
        }

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