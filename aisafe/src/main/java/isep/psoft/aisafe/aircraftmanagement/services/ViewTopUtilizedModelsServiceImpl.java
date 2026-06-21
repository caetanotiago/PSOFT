package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.dto.AircraftModelUtilizationDTO;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftModelRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewTopUtilizedModelsServiceImpl implements ViewTopUtilizedModelsService {

    private final AircraftModelRepository aircraftModelRepository;

    public ViewTopUtilizedModelsServiceImpl(AircraftModelRepository aircraftModelRepository) {
        this.aircraftModelRepository = aircraftModelRepository;
    }

    @Override
    public List<AircraftModelUtilizationDTO> getTopUtilizedModels(String metric) {
        PageRequest top5 = PageRequest.of(0, 5);

        if ("flight_hours".equalsIgnoreCase(metric)) {
            return aircraftModelRepository.findTopModelsByFlightHours(top5);
        }
        // default: assignments
        return aircraftModelRepository.findTopModelsByAssignments(top5);
    }
}