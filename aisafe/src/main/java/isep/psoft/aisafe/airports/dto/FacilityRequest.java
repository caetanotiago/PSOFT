package isep.psoft.aisafe.airports.dto;

import jakarta.validation.constraints.NotBlank;

// US207: shape reused both by the additive POST /facilities endpoint (as AddFacilityRequest)
// and as an optional array field of RegisterAirportRequest when supplied at creation time.
public record FacilityRequest(
        @NotBlank(message = "Facility type is required") String type,
        @NotBlank(message = "Facility identifier is required") String identifier,
        String description
) {}
