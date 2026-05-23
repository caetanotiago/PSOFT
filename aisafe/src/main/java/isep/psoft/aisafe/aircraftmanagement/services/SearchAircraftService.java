package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchAircraftService {
    Page<Aircraft> searchAircrafts(String modelName, String status, Integer year, Pageable pageable);
}