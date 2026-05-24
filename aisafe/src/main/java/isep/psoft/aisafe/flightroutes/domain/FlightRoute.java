package isep.psoft.aisafe.flightroutes.domain;

import isep.psoft.aisafe.airports.domain.Airport; 
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) 
public class FlightRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id; // O Route ID gerado pelo sistema 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_iata", nullable = false)
    private Airport origin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_iata", nullable = false)
    private Airport destination;

    @Embedded
    private RouteDistance distance;

    @Embedded
    private RouteRequirements requirements;

    @Embedded
    private EstimatedFlightTime estimatedFlightTime;

    @Embedded
    private RouteStatus status;

    @Version
    private Long version; // Optimistic Locking (Impede conflitos de concorrência)

    @ElementCollection
    @CollectionTable(name = "ROUTE_HISTORY_LOG", joinColumns = @JoinColumn(name = "ROUTE_ID"))
    private List<RouteHistory> historyLog = new ArrayList<>();

    // CORREÇÃO: O construtor precisa de ser público para ser chamado pela Factory, que está noutro pacote.
    public FlightRoute(Airport origin, Airport destination, RouteDistance distance, 
                          RouteRequirements requirements, EstimatedFlightTime estimatedFlightTime) {
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
        this.requirements = requirements;
        this.estimatedFlightTime = estimatedFlightTime;
        this.status = RouteStatus.active(); // Uma rota nasce sempre ativa
        
        // Regista a criação no histórico (US111)
        this.historyLog.add(new RouteHistory("Route created.", distance.getDistance()));
    }

    // US112 (Update) 
    public void updateDetails(RouteRequirements newRequirements, EstimatedFlightTime newTime) {
        this.requirements = newRequirements;
        this.estimatedFlightTime = newTime;
        
        // Adiciona automaticamente o registo de histórico
        this.historyLog.add(new RouteHistory("Route requirements/time updated.", this.distance.getDistance()));
    }

    // US112 (Deactivate)
    public void deactivate() {
        this.status = RouteStatus.inactive();
        this.historyLog.add(new RouteHistory("Route deactivated.", this.distance.getDistance()));
    }
}
