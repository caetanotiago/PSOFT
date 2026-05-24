package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.Airport;

public interface ViewAirportDetailsUseCase {

    Airport getAirportByIataCode(String iataCode);
}
