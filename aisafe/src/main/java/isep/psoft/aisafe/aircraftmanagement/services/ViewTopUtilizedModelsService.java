package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.dto.AircraftModelUtilizationDTO;
import java.util.List;

public interface ViewTopUtilizedModelsService {

    List<AircraftModelUtilizationDTO> getTopUtilizedModels(String metric);
}