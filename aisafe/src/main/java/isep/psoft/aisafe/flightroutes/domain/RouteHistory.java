package isep.psoft.aisafe.flightroutes.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entrada (imutável) do histórico de alterações de uma rota (US111).
 * Regista sempre {@code changeDate} + {@code description} e, dinamicamente, apenas os valores
 * ANTERIORES dos atributos efetivamente alterados (os restantes ficam a {@code null} e são
 * omitidos do JSON pelo {@code @JsonInclude(NON_NULL)} do DTO).
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RouteHistory {

    private LocalDateTime changeDate;
    private String description;

    // Valores anteriores — apenas preenchidos para os atributos realmente alterados.
    private Double previousMinRange;
    private Integer previousMinCapacity;
    private Integer previousEstimatedFlightTime;
    private String previousStatus;

    private RouteHistory(String description, Double previousMinRange, Integer previousMinCapacity,
                         Integer previousEstimatedFlightTime, String previousStatus) {
        this.changeDate = LocalDateTime.now();
        this.description = description;
        this.previousMinRange = previousMinRange;
        this.previousMinCapacity = previousMinCapacity;
        this.previousEstimatedFlightTime = previousEstimatedFlightTime;
        this.previousStatus = previousStatus;
    }

    /** Registo da criação da rota — apenas data + descrição. */
    public static RouteHistory created() {
        return new RouteHistory("Route created.", null, null, null, null);
    }

    /**
     * Registo de atualização de detalhes operacionais. Cada parâmetro só deve vir preenchido
     * quando o respetivo atributo foi realmente alterado (caso contrário {@code null}).
     */
    public static RouteHistory detailsUpdated(Double previousMinRange, Integer previousMinCapacity,
                                              Integer previousEstimatedFlightTime) {
        return new RouteHistory("Route details updated.",
                previousMinRange, previousMinCapacity, previousEstimatedFlightTime, null);
    }

    /** Registo de mudança de estado (ativação/desativação) — guarda o estado anterior. */
    public static RouteHistory statusChanged(String previousStatus, String description) {
        return new RouteHistory(description, null, null, null, previousStatus);
    }
}
