package isep.psoft.aisafe.aircraftmanagement.assemblers;

import isep.psoft.aisafe.aircraftmanagement.domain.AircraftModel;
import isep.psoft.aisafe.aircraftmanagement.dto.AircraftModelDTO;
import org.springframework.stereotype.Component;

@Component
public class AircraftModelAssembler {

    public AircraftModelDTO toDTO(AircraftModel model) {
        AircraftModelDTO dto = new AircraftModelDTO();
        
        dto.setId(model.getId());
        dto.setManufacturer(model.getModelDesignation().getManufacturer());
        dto.setModelName(model.getModelDesignation().getModelName());
        
        dto.setStandardCapacity(model.getSpecifications().getStandardCapacity());
        dto.setFuelCapacity(model.getSpecifications().getFuelCapacity());
        dto.setMaximumRange(model.getSpecifications().getMaximumRange());
        dto.setCruisingSpeed(model.getSpecifications().getCruisingSpeed());
        
        return dto;
    }
}