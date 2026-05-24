package isep.psoft.aisafe.maintenance.services.us115;

import isep.psoft.aisafe.aircraftmanagement.domain.AircraftModel;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftModelRepository;
import isep.psoft.aisafe.maintenance.assemblers.TemplateAssembler;
import isep.psoft.aisafe.maintenance.domain.MaintenanceInterval;
import isep.psoft.aisafe.maintenance.domain.MaintenanceTemplate;
import isep.psoft.aisafe.maintenance.domain.TemplateType;
import isep.psoft.aisafe.maintenance.dto.CreateTemplateDTO;
import isep.psoft.aisafe.maintenance.dto.TemplateDTO;
import isep.psoft.aisafe.maintenance.repositories.MaintenanceTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("CreateMaintenanceTemplateUseCase")
public class CreateMaintenanceTemplateUseCase {

    @Autowired
    private MaintenanceTemplateRepository templateRepository;
    @Autowired
    private AircraftModelRepository aircraftModelRepository;
    @Autowired
    private TemplateAssembler assembler;

    @Transactional
    public TemplateDTO execute(CreateTemplateDTO dto) {
        if (templateRepository.findByTemplateName(dto.getTemplateName()).isPresent()) {
            throw new IllegalArgumentException("A template with this name already exists.");
        }

        TemplateType type = TemplateType.valueOf(dto.getTemplateType().toUpperCase());
        MaintenanceInterval interval = new MaintenanceInterval(dto.getFlightHours(), dto.getCalendarDays());

        List<AircraftModel> foundModels = new ArrayList<>();
        for (String modelName : dto.getApplicableModels()) {
            AircraftModel model = aircraftModelRepository.findByDesignationModelName(modelName)
                    .orElseThrow(() -> new IllegalArgumentException("Aircraft Model '" + modelName + "' not found."));
            foundModels.add(model);
        }

        if (foundModels.isEmpty()) {
            throw new IllegalArgumentException("At least one valid Aircraft Model must be provided.");
        }

        MaintenanceTemplate newTemplate = new MaintenanceTemplate(
                dto.getTemplateName(),
                type,
                interval,
                dto.getChecklist(),
                foundModels
        );

        MaintenanceTemplate savedTemplate = templateRepository.save(newTemplate);
        
        return assembler.toDTO(savedTemplate);
    }
}
