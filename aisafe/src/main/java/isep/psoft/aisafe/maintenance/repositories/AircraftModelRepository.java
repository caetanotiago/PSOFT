package isep.psoft.aisafe.Maintenance.repositories;

import isep.psoft.aisafe.aircraft.domain.AircraftModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AircraftModelRepository extends JpaRepository<AircraftModel, Long> {
    // Procura todos os modelos de avião que correspondam a uma lista de nomes
    List<AircraftModel> findByDesignationIn(List<String> designations);
}