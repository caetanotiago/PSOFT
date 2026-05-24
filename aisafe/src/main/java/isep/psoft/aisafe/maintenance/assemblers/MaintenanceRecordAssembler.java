package isep.psoft.aisafe.maintenance.assemblers;

import isep.psoft.aisafe.maintenance.controllers.MaintenanceController;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MaintenanceRecordAssembler {

    public MaintenanceRecordOutputDto toModel(MaintenanceRecord entity) {
        MaintenanceRecordOutputDto dto = new MaintenanceRecordOutputDto();

        dto.setId(entity.getId());
        dto.setAircraftRegistration(entity.getAircraftRegistration());
        dto.setTemplateId(entity.getMaintenanceTemplateId());

        if (entity.getRecordDetails() != null) {
            dto.setDescription(entity.getRecordDetails().getDescription());
            dto.setStartDate(entity.getRecordDetails().getStartDate());
            dto.setExpectedDurationMinutes(entity.getRecordDetails().getExpectedDurationMinutes());
        }

        if (entity.getComponent() != null) {
            dto.setComponentCategory(entity.getComponent().getCategory());
        }

        if (entity.getCompletionNotes() != null) {
            dto.setCompletionNotes(entity.getCompletionNotes().getNotes());
        }

        dto.add(linkTo(methodOn(MaintenanceController.class).getRecordById(entity.getId())).withSelfRel());

        dto.add(linkTo(methodOn(MaintenanceController.class).getRecordsByAircraft(entity.getAircraftRegistration())).withRel("aircraft-records"));

        return dto;
    }
}