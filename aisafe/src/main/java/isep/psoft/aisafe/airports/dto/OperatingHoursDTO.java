package isep.psoft.aisafe.airports.dto;

import isep.psoft.aisafe.airports.domain.OperatingHours;

import java.time.LocalTime;

public record OperatingHoursDTO(boolean operates24Hours, LocalTime opens, LocalTime closes) {

    public static OperatingHoursDTO from(OperatingHours hours) {
        return new OperatingHoursDTO(hours.isOperates24Hours(), hours.getOpens(), hours.getCloses());
    }
}
