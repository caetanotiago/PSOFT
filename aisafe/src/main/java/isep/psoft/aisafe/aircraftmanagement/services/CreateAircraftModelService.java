package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.domain.AircraftModel;
import isep.psoft.aisafe.aircraftmanagement.dto.CreateAircraftModelDTO;

public interface CreateAircraftModelService {
    AircraftModel createAircraftModel(CreateAircraftModelDTO dto);
}