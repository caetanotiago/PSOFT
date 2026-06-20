package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.Airport;
import isep.psoft.aisafe.airports.domain.AirportContact;
import isep.psoft.aisafe.airports.domain.AirportNotFoundException;
import isep.psoft.aisafe.airports.domain.IATACode;
import isep.psoft.aisafe.airports.domain.OperatingHours;
import isep.psoft.aisafe.airports.dto.AirportContactRequest;
import isep.psoft.aisafe.airports.dto.OperatingHoursRequest;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UpdateAirportDetailsUseCaseImpl implements UpdateAirportDetailsUseCase {

    private final AirportRepository airportRepository;

    public UpdateAirportDetailsUseCaseImpl(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Override
    public Airport updateDetails(String iataCode, OperatingHoursRequest operatingHours, List<AirportContactRequest> contacts) {
        if (operatingHours == null && contacts == null)
            throw new IllegalArgumentException("At least one of operatingHours or contacts must be supplied");

        IATACode key = new IATACode(iataCode.toUpperCase());
        Airport airport = airportRepository.findById(key)
                .orElseThrow(() -> new AirportNotFoundException(iataCode));

        if (operatingHours != null) {
            airport.updateOperatingHours(new OperatingHours(
                    operatingHours.operates24Hours(), operatingHours.opens(), operatingHours.closes()));
        }

        if (contacts != null) {
            airport.updateContacts(contacts.stream()
                    .map(c -> new AirportContact(c.type(), c.value(), c.description()))
                    .toList());
        }

        return airportRepository.save(airport);
    }
}
