package isep.psoft.aisafe.maintenance.dto;

import isep.psoft.aisafe.aircraft.domain.AircraftModel;
import isep.psoft.aisafe.maintenance.domain.MaintenanceTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TemplateAssembler {

    public TemplateDTO toDTO(MaintenanceTemplate template) {
        // Extrai apenas as designações (nomes) dos modelos de avião para enviar na resposta
        List<String> modelDesignations = template.getApplicableModels().stream()
                // Nota: O getDesignation() tem de existir na classe AircraftModel do teu colega!
                .map(AircraftModel::getDesignation)
                .collect(Collectors.toList());

        return new TemplateDTO(
                template.getId(),
                template.getTemplateName(),
                template.getTemplateType().name(),
                template.getInterval() != null ? template.getInterval().getFlightHours() : null,
                template.getInterval() != null ? template.getInterval().getCalendarDays() : null,
                template.getChecklist(),
                modelDesignations
        );
    }
}