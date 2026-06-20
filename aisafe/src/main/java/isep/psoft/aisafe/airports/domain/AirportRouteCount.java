package isep.psoft.aisafe.airports.domain;

// US210: read projection pairing an Airport with its derived route count
// (number of FlightRoute rows where the airport is origin or destination).
// Not persisted — mirrors RouteUsage (US214) for route popularity.
public class AirportRouteCount {

    private final Airport airport;
    private final long routeCount;

    public AirportRouteCount(Airport airport, long routeCount) {
        this.airport = airport;
        this.routeCount = routeCount;
    }

    public Airport getAirport() { return airport; }
    public long getRouteCount() { return routeCount; }
}
