package isep.psoft.aisafe.airports.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class AirportDetails {

    private String name;
    private String city;
    private String country;
    private String region;
    private String timezone;

    @Embedded
    private Coordinates coordinates;

    protected AirportDetails() {}

    public AirportDetails(String name, String city, String country, String region, String timezone, Coordinates coordinates) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.region = region;
        this.timezone = timezone;
        this.coordinates = coordinates;
    }

    public String getName() { return name; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public String getRegion() { return region; }
    public String getTimezone() { return timezone; }
    public Coordinates getCoordinates() { return coordinates; }
}
