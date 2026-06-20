package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.Airport;

public interface AddPhotoUseCase {

    Airport addPhoto(String iataCode, String url, String caption);
}
