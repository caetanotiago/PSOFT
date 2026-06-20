package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.Airport;
import isep.psoft.aisafe.airports.domain.AirportNotFoundException;
import isep.psoft.aisafe.airports.domain.IATACode;
import isep.psoft.aisafe.airports.domain.Photo;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AddPhotoUseCaseImpl implements AddPhotoUseCase {

    private final AirportRepository airportRepository;

    public AddPhotoUseCaseImpl(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Override
    public Airport addPhoto(String iataCode, String url, String caption) {
        IATACode key = new IATACode(iataCode.toUpperCase());
        Airport airport = airportRepository.findById(key)
                .orElseThrow(() -> new AirportNotFoundException(iataCode));

        airport.addPhoto(new Photo(url, caption));
        return airportRepository.save(airport);
    }
}
