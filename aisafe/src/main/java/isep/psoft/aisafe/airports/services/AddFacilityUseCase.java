package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.Airport;

public interface AddFacilityUseCase {

    Airport addFacility(String iataCode, String type, String identifier, String description);
}
