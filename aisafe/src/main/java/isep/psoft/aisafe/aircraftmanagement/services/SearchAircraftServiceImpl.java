package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import isep.psoft.aisafe.aircraftmanagement.specifications.AircraftSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SearchAircraftServiceImpl implements SearchAircraftService {

    private final AircraftRepository aircraftRepository;

    public SearchAircraftServiceImpl(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    @Override
    public Page<Aircraft> searchAircrafts(String modelName, String status, Integer year, Pageable pageable) {
        // O findAll() do JpaSpecificationExecutor processa a nossa Specification
        return aircraftRepository.findAll(AircraftSpecification.build(modelName, status, year), pageable);
    }
}