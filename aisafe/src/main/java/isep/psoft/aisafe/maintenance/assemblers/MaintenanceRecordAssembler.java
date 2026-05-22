package isep.psoft.aisafe.maintenance.assemblers;

import isep.psoft.aisafe.maintenance.controllers.MaintenanceController;
import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.dto.CompleteRecordInputDto;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MaintenanceRecordAssembler extends RepresentationModelAssemblerSupport<MaintenanceRecord, MaintenanceRecordOutputDto> {

    public MaintenanceRecordAssembler() {
        super(MaintenanceController.class, MaintenanceRecordOutputDto.class);
    }

    @Override
    public MaintenanceRecordOutputDto toModel(MaintenanceRecord entity) {
        MaintenanceRecordOutputDto dto = new MaintenanceRecordOutputDto(
                entity.getId(),
                entity.getAircraftRegistration(),
                entity.getMaintenanceTemplateId(),
                entity.getRecordDetails(),
                entity.getComponent(),
                entity.getCompletionNotes()
        );

        // Self link
        dto.add(linkTo(methodOn(MaintenanceController.class).getRecordById(entity.getId())).withSelfRel());

        // Link to complete the record (US119), only if not already completed
        if (entity.getCompletionNotes() == null) {
            // Solução Definitiva: Fornecer um valor não-nulo e do tipo correto para TODOS os argumentos
            // para que o Spring HATEOAS possa encontrar o método sem qualquer ambiguidade.
            String dummyVersion = entity.getVersion() != null ? entity.getVersion().toString() : "0";

            dto.add(linkTo(methodOn(MaintenanceController.class)
                    .completeMaintenanceRecord(entity.getId(), new CompleteRecordInputDto(), dummyVersion))
                    .withRel("complete"));
        }

        return dto;
    }
}
