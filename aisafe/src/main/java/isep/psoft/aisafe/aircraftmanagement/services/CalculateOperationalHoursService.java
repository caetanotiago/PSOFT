package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.dto.AircraftOperationalHoursDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CalculateOperationalHoursService {
    Page<AircraftOperationalHoursDTO> calculateOperationalHours(Pageable pageable);
}