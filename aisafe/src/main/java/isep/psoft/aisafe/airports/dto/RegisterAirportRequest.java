package isep.psoft.aisafe.airports.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record RegisterAirportRequest(
        @NotBlank(message = "IATA code is required")
        @Pattern(regexp = "[A-Z]{3}", message = "IATA code must be exactly 3 uppercase letters")
        String iataCode,

        @NotBlank(message = "Airport name is required") String name,
        @NotBlank(message = "City is required") String city,
        @NotBlank(message = "Country is required") String country,
        String region,
        @NotBlank(message = "Timezone is required") String timezone,

        @NotNull(message = "Latitude is required")
        @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
        @DecimalMax(value = "90.0",  message = "Latitude must be <= 90")
        Double latitude,

        @NotNull(message = "Longitude is required")
        @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
        @DecimalMax(value = "180.0",  message = "Longitude must be <= 180")
        Double longitude,

        @NotEmpty(message = "At least one runway is required")
        @Valid List<RunwayRequest> runways
) {}
