package isep.psoft.aisafe.airports.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Coordinates {

    private Double latitude;
    private Double longitude;

    protected Coordinates() {}

    public Coordinates(Double latitude, Double longitude) {
        if (latitude == null || latitude < -90 || latitude > 90)
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        if (longitude == null || longitude < -180 || longitude > 180)
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
}
