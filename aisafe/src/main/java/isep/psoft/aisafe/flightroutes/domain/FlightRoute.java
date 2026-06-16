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
        this.historyLog.add(RouteHistory.created());
    }

    // US112 (Update) — atualização parcial dos detalhes operacionais.
    // Cada parâmetro a null = atributo não alterado. Regista no histórico apenas os valores
    // anteriores dos atributos efetivamente alterados (US111 — histórico dinâmico).
    public void updateDetails(Double newMinRange, Integer newMinCapacity, Integer newTime) {
        Double effMinRange = this.requirements.getMinRange();
        Integer effMinCapacity = this.requirements.getMinCapacity();
        Integer effTime = this.estimatedFlightTime.getDurationMinutes();

        Double prevMinRange = null;
        Integer prevMinCapacity = null;
        Integer prevTime = null;

        if (newMinRange != null && !newMinRange.equals(effMinRange)) {
            prevMinRange = effMinRange;
            effMinRange = newMinRange;
        }
        if (newMinCapacity != null && !newMinCapacity.equals(effMinCapacity)) {
            prevMinCapacity = effMinCapacity;
            effMinCapacity = newMinCapacity;
        }
        if (newTime != null && !newTime.equals(effTime)) {
            prevTime = effTime;
            effTime = newTime;
        }

        // Nada mudou de facto → não altera estado nem regista histórico.
        if (prevMinRange == null && prevMinCapacity == null && prevTime == null) {
            return;
        }

        this.requirements = new RouteRequirements(effMinRange, effMinCapacity);
        this.estimatedFlightTime = new EstimatedFlightTime(effTime);
        this.historyLog.add(RouteHistory.detailsUpdated(prevMinRange, prevMinCapacity, prevTime));
    }

    // US112 (Activate/Deactivate) — "deactivate a route" = mudar o status para INACTIVE.
    public void changeStatus(String newState) {
        String current = this.status.getState();

        if ("INACTIVE".equalsIgnoreCase(newState)) {
            if ("INACTIVE".equalsIgnoreCase(current)) {
                throw new IllegalStateException("Route is already INACTIVE.");
            }
            this.status = RouteStatus.inactive();
            this.historyLog.add(RouteHistory.statusChanged(current, "Route deactivated."));
        } else if ("ACTIVE".equalsIgnoreCase(newState)) {
            if ("ACTIVE".equalsIgnoreCase(current)) {
                throw new IllegalStateException("Route is already ACTIVE.");
            }
            this.status = RouteStatus.active();
            this.historyLog.add(RouteHistory.statusChanged(current, "Route activated."));
        } else {
            throw new IllegalArgumentException("Invalid status: " + newState + " (must be ACTIVE or INACTIVE).");
        }
    }
}
