package isep.psoft.aisafe.aircraftmanagement.repositories;

import isep.psoft.aisafe.flightroutes.domain.ScheduledFlight;
import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;
import isep.psoft.aisafe.aircraftmanagement.dto.AircraftOperationalHoursDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;


public interface AircraftRepository extends CrudRepository<Aircraft, Long>, JpaSpecificationExecutor<Aircraft> {

    List<Aircraft> findAll();

    Optional<Aircraft> findByRegistration_Registration(String registration);

    // Query JPQL Otimizada: Soma a duração dos voos por avião. 
    @Query("SELECT new isep.psoft.aisafe.aircraftmanagement.dto.AircraftOperationalHoursDTO(" +
           "a.registration.registration, COALESCE(SUM(f.estimatedFlightTime.durationMinutes), 0L)) " +
           "FROM Aircraft a LEFT JOIN ScheduledFlight f ON f.aircraft = a " +
           "GROUP BY a.registration.registration")
    Page<AircraftOperationalHoursDTO> findOperationalHoursPerAircraft(Pageable pageable);
}