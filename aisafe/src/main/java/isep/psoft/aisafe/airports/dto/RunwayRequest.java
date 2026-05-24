package isep.psoft.aisafe.airports.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RunwayRequest(
        @NotBlank(message = "Runway name is required") String name,
        @NotNull @Positive(message = "Runway length must be a positive number (metres)") Double length,
        @NotBlank(message = "Runway orientation is required") String orientation
) {}
