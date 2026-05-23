package isep.psoft.aisafe.aircraftmanagement.repositories;

import isep.psoft.aisafe.aircraftmanagement.domain.AircraftModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AircraftModelRepository extends CrudRepository<AircraftModel, Long> {
    Optional<AircraftModel> findByDesignationModelName(String modelName);
}