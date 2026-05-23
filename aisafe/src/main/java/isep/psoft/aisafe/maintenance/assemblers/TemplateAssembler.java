package isep.psoft.aisafe.maintenance.assemblers;

// 1. IMPORT CORRIGIDO: Agora aponta para a pasta do teu colega (aircraftmanagement)
import isep.psoft.aisafe.aircraftmanagement.domain.AircraftModel;
import isep.psoft.aisafe.maintenance.domain.MaintenanceTemplate;
import isep.psoft.aisafe.maintenance.dto.TemplateDTO; // Import adicionado porque mudámos a package
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TemplateAssembler {

    public TemplateDTO toDTO(MaintenanceTemplate template) {
        // Extrai apenas as designações (nomes) dos modelos de avião para enviar na resposta
        List<String> modelDesignations = template.getApplicableModels().stream()
                // Como o teu colega já adicionou o getDesignation() na classe dele, isto vai funcionar na perfeição!
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