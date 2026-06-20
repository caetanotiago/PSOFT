package isep.psoft.aisafe.airports.domain;

import java.util.List;

// US211: read projection grouping Airports by a key (region or country).
// Not persisted; HATEOAS/DTO assembly happens in the controller (presentation layer concern).
public class AirportGroup {

    private final String groupKey;
    private final List<Airport> airports;

    public AirportGroup(String groupKey, List<Airport> airports) {
        this.groupKey = groupKey;
        this.airports = airports;
    }

    public String getGroupKey() { return groupKey; }
    public List<Airport> getAirports() { return airports; }
}
