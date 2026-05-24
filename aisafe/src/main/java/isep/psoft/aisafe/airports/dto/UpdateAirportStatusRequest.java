package isep.psoft.aisafe.airports.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateAirportStatusRequest(
        @NotBlank(message = "State is required (OPERATIONAL | CLOSED | UNDER_MAINTENANCE)")
        String state
) {}
