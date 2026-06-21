package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.dto.AircraftStatusDTO;

public interface ViewAircraftStatusService {

    AircraftStatusDTO getAircraftStatus(String registrationNumber);
}