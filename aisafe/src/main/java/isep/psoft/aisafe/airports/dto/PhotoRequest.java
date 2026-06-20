package isep.psoft.aisafe.airports.dto;

import jakarta.validation.constraints.NotBlank;

public record PhotoRequest(
        @NotBlank(message = "Photo URL is required") String url,
        String caption
) {}
