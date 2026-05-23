package isep.psoft.aisafe.aircraftmanagement.specifications;

import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AircraftSpecification {

    public static Specification<Aircraft> build(String modelName, String status, Integer year) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por nome do modelo (Usando JOIN explícito para atravessar o @ManyToOne e não crashar)
            if (modelName != null && !modelName.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.join("model").get("designation").get("modelName")),
                        modelName.toLowerCase()
                ));
            }

            // Filtro por Status
            if (status != null && !status.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.upper(root.get("status").get("state")),
                        status.toUpperCase()
                ));
            }

            // Filtro por Ano de Fabrico (usando um 'between' para garantir compatibilidade com diferentes BDs)
            if (year != null) {
                predicates.add(criteriaBuilder.between(root.get("manufacturingDate").get("date"), LocalDate.of(year, 1, 1), LocalDate.of(year, 12, 31)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}