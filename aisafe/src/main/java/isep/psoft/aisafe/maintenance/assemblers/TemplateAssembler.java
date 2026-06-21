package isep.psoft.aisafe.maintenance.assemblers;

import isep.psoft.aisafe.aircraftmanagement.domain.AircraftModel;
import isep.psoft.aisafe.maintenance.controllers.MaintenanceTemplateRestController;
import isep.psoft.aisafe.maintenance.domain.MaintenanceTemplate;
import isep.psoft.aisafe.maintenance.dto.TemplateDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TemplateAssembler {

    public TemplateDTO toDTO(MaintenanceTemplate template) {

        // CORREÇÃO: Em vez de adivinharmos o nome do método do teu colega,
        // vamos usar o getId() e convertê-lo para String. Assim nunca falha!
        List<String> modelDesignations = template.getApplicableModels().stream()
                .map(model -> String.valueOf(model.getId()))
                .collect(Collectors.toList());

        TemplateDTO dto = new TemplateDTO(
                template.getId(),
                template.getTemplateName(),
                template.getTemplateType().name(),
                template.getInterval() != null ? template.getInterval().getFlightHours() : null,
                template.getInterval() != null ? template.getInterval().getCalendarDays() : null,
                template.getChecklist(),
                modelDesignations
        );

        dto.add(linkTo(methodOn(MaintenanceTemplateRestController.class).getTemplateById(template.getId())).withSelfRel());

        return dto;
    }
}