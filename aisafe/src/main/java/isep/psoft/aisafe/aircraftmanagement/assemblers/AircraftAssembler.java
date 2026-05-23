package isep.psoft.aisafe.aircraftmanagement.assemblers;

import isep.psoft.aisafe.aircraftmanagement.controllers.AircraftRestController;
import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;
import isep.psoft.aisafe.aircraftmanagement.dto.AircraftDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AircraftAssembler implements RepresentationModelAssembler<Aircraft, EntityModel<AircraftDTO>> {

    @Override
    public EntityModel<AircraftDTO> toModel(Aircraft entity) {
        AircraftDTO dto = new AircraftDTO();
        
        dto.setRegistrationNumber(entity.getRegistrationNumber().getNumber());
        dto.setModelName(entity.getModel().getModelDesignation().getModelName());
        dto.setManufacturer(entity.getModel().getModelDesignation().getManufacturer());
        dto.setManufacturingDate(entity.getManufacturingDate().getDate());
        dto.setSeatingCapacity(entity.getSeatingCapacity().getTotalSeats());
        dto.setStatus(entity.getStatus().getState());
        dto.setVersion(entity.getVersion()); // Importante para o Optimistic Locking da US105

        EntityModel<AircraftDTO> model = EntityModel.of(dto);

        // Adiciona Self-Link dinamicamente para o endpoint da US103
        model.add(linkTo(methodOn(AircraftRestController.class)
                .getAircraft(dto.getRegistrationNumber())).withSelfRel());

        // Adiciona link para o Aircraft Model (conforme Acceptance Criteria da US103)
        model.add(Link.of("/api/aircraft-models/" + dto.getModelName())
                .withRel("aircraft-model"));

        return model;
    }
}