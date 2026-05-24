package isep.psoft.aisafe.airports.dto;

import isep.psoft.aisafe.airports.domain.Airport;

public record AirportSummaryResponseDTO(
        String iataCode,
        String name,
        String city,
        String country,
        String status
) {
    public static AirportSummaryResponseDTO from(Airport airport) {
        return new AirportSummaryResponseDTO(
                airport.getIataCode().getCode(),
                airport.getDetails().getName(),
                airport.getDetails().getCity(),
                airport.getDetails().getCountry(),
                airport.getStatus().name()
        );
    }
}
