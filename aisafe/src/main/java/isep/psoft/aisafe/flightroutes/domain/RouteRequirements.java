package isep.psoft.aisafe.flightroutes.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Exigido pelo JPA
public class RouteRequirements {

    private Double minRange;
    private Integer minCapacity;

    public RouteRequirements(Double minRange, Integer minCapacity) {
        if (minRange == null || minRange <= 0) {
            throw new IllegalArgumentException("Minimum range must be greater than zero.");
        }
        if (minCapacity == null || minCapacity <= 0) {
            throw new IllegalArgumentException("Minimum capacity must be greater than zero.");
        }
        this.minRange = minRange;
        this.minCapacity = minCapacity;
    }
}