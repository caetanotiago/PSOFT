package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.AirportGroup;

import java.util.List;

public interface GroupAirportsUseCase {

    List<AirportGroup> groupBy(String criterion);
}
