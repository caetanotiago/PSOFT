package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;

public interface ViewAircraftService {
    Aircraft getAircraftByRegistrationNumber(String registrationNumber);
}