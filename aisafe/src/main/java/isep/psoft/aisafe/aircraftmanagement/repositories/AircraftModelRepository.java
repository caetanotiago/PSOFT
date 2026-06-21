package isep.psoft.aisafe.aircraftmanagement.repositories;

import isep.psoft.aisafe.aircraftmanagement.domain.AircraftModel;
import isep.psoft.aisafe.aircraftmanagement.dto.AircraftModelUtilizationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AircraftModelRepository extends CrudRepository<AircraftModel, Long> {

    Optional<AircraftModel> findByDesignationModelName(String modelName);

    @Query("SELECT new isep.psoft.aisafe.aircraftmanagement.dto.AircraftModelUtilizationDTO(" +
           "a.model.designation.modelName, COUNT(sf)) " +
           "FROM Aircraft a JOIN ScheduledFlight sf ON sf.aircraftRegistration = a.registrationNumber.number " +
           "GROUP BY a.model.designation.modelName ORDER BY COUNT(sf) DESC")
    List<AircraftModelUtilizationDTO> findTopModelsByAssignments(Pageable pageable);

    @Query("SELECT new isep.psoft.aisafe.aircraftmanagement.dto.AircraftModelUtilizationDTO(" +
           "a.model.designation.modelName, SUM(sf.route.estimatedFlightTime.durationMinutes)) " +
           "FROM Aircraft a JOIN ScheduledFlight sf ON sf.aircraftRegistration = a.registrationNumber.number " +
           "GROUP BY a.model.designation.modelName ORDER BY SUM(sf.route.estimatedFlightTime.durationMinutes) DESC")
    List<AircraftModelUtilizationDTO> findTopModelsByFlightHours(Pageable pageable);
}