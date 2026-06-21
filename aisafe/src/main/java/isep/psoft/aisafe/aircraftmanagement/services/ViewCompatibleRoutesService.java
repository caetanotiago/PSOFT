package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.flightroutes.dto.FlightRouteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ViewCompatibleRoutesService {

    Page<FlightRouteDTO> getCompatibleRoutes(String registrationNumber, Pageable pageable);
}