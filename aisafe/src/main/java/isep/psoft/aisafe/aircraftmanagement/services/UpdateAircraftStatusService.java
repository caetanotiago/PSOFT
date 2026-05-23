package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;

public interface UpdateAircraftStatusService {
    Aircraft updateStatus(String registrationNumber, String newStatus, Long version);
}