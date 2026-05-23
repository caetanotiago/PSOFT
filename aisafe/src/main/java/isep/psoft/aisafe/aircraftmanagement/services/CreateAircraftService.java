package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;
import isep.psoft.aisafe.aircraftmanagement.dto.CreateAircraftDTO;

public interface CreateAircraftService {
    Aircraft createAircraft(CreateAircraftDTO dto);
}