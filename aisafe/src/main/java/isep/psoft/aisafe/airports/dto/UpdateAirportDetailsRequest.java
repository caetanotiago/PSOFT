package isep.psoft.aisafe.airports.dto;

import jakarta.validation.Valid;

import java.util.List;

// US208: both fields optional, but at least one must be present (validated in the use case, AC9).
public record UpdateAirportDetailsRequest(
        @Valid OperatingHoursRequest operatingHours,
        @Valid List<AirportContactRequest> contacts
) {}
