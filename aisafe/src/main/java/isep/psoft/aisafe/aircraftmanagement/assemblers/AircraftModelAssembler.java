package isep.psoft.aisafe.aircraftmanagement.assemblers;

import isep.psoft.aisafe.aircraftmanagement.controllers.AircraftModelRestController;
import isep.psoft.aisafe.aircraftmanagement.domain.AircraftModel;
import isep.psoft.aisafe.aircraftmanagement.dto.AircraftModelDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AircraftModelAssembler
        implements RepresentationModelAssembler<AircraftModel, EntityModel<AircraftModelDTO>> {

    @Override
    public EntityModel<AircraftModelDTO> toModel(AircraftModel model) {
        AircraftModelDTO dto = toDTO(model);
        return EntityModel.of(dto,
                linkTo(methodOn(AircraftModelRestController.class)
                        .getAircraftModel(model.getModelDesignation().getModelName())).withSelfRel());
    }

    public AircraftModelDTO toDTO(AircraftModel model) {
        AircraftModelDTO dto = new AircraftModelDTO();
        dto.setId(model.getId());
        dto.setManufacturer(model.getModelDesignation().getManufacturer());
        dto.setModelName(model.getModelDesignation().getModelName());
        dto.setStandardCapacity(model.getSpecifications().getStandardCapacity());
        dto.setFuelCapacity(model.getSpecifications().getFuelCapacity());
        dto.setMaximumRange(model.getSpecifications().getMaximumRange());
        dto.setCruisingSpeed(model.getSpecifications().getCruisingSpeed());
        dto.setVersion(model.getVersion());
        if (model.getImage() != null) {
            dto.setImage(model.getImage().getImageData());
        }
        return dto;
    }
}