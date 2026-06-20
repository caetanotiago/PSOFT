package isep.psoft.aisafe.airports.dto;

import isep.psoft.aisafe.airports.domain.AirportRouteCount;

public record AirportRouteCountDTO(String iataCode, String name, long routeCount) {

    public static AirportRouteCountDTO from(AirportRouteCount count) {
        return new AirportRouteCountDTO(
                count.getAirport().getIataCode().getCode(),
                count.getAirport().getDetails().getName(),
                count.getRouteCount()
        );
    }
}
