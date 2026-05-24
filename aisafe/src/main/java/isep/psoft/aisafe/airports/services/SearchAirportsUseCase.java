package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.Airport;

import java.util.List;

public interface SearchAirportsUseCase {

    List<Airport> searchAirports(String name, String city, String country);
}
