package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.domain.AircraftModel;
import isep.psoft.aisafe.aircraftmanagement.dto.UpdateModelSpecificationsDTO;

public interface UpdateAircraftModelService {

    AircraftModel findByModelName(String modelName);

    AircraftModel updateSpecifications(String modelName, Long expectedVersion,
                                       UpdateModelSpecificationsDTO dto);
}