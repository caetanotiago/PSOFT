package isep.psoft.aisafe.airports.dto;

import jakarta.validation.constraints.NotBlank;

public record AirportContactRequest(
        @NotBlank(message = "Contact type is required") String type,
        @NotBlank(message = "Contact value is required") String value,
        String description
) {}
