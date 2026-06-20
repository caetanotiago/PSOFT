package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.AirportRouteCount;

import java.util.List;

public interface ListBusiestAirportsUseCase {

    List<AirportRouteCount> listBusiest(Integer limit);
}
