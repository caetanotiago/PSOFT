package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.Airport;
import isep.psoft.aisafe.airports.dto.AirportContactRequest;
import isep.psoft.aisafe.airports.dto.OperatingHoursRequest;

import java.util.List;

public interface UpdateAirportDetailsUseCase {

    Airport updateDetails(String iataCode, OperatingHoursRequest operatingHours, List<AirportContactRequest> contacts);
}
