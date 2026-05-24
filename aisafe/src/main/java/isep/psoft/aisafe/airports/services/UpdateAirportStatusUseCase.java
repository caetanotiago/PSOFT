package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.Airport;

public interface UpdateAirportStatusUseCase {

    Airport updateStatus(String iataCode, String newStateStr);
}
