package isep.psoft.aisafe.flightroutes.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RouteHistory {

    private LocalDateTime changeDate;
    private String description;
    private Double previousDistance; // Opcional, caso a distância não mude

    public RouteHistory(String description, Double previousDistance) {
        this.changeDate = LocalDateTime.now();
        this.description = description;
        this.previousDistance = previousDistance;
    }
}