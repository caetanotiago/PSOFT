package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.Airport;

public interface AddCertificationUseCase {

    Airport addCertification(String iataCode, String manufacturer, String modelName);
}
