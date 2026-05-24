package isep.psoft.aisafe.airports.dto;

import isep.psoft.aisafe.airports.domain.Runway;

public record RunwayDTO(String name, Double length, String orientation) {

    public static RunwayDTO from(Runway runway) {
        return new RunwayDTO(runway.getName(), runway.getLength(), runway.getOrientation());
    }
}
