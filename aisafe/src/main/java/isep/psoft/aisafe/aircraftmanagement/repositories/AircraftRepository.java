package isep.psoft.aisafe.aircraftmanagement.repositories;

import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;
import isep.psoft.aisafe.aircraftmanagement.domain.RegistrationNumber;
import isep.psoft.aisafe.aircraftmanagement.dto.AircraftOperationalHoursDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;


public interface AircraftRepository extends CrudRepository<Aircraft, RegistrationNumber>, JpaSpecificationExecutor<Aircraft> {

    List<Aircraft> findAll();
    @Query("SELECT a FROM Aircraft a WHERE a.registrationNumber.number = ?1")
    Optional<Aircraft> findByRegistration_Registration(String registration);

    // Query JPQL Otimizada: Soma a duração dos voos por avião. 
    @Query("SELECT new isep.psoft.aisafe.aircraftmanagement.dto.AircraftOperationalHoursDTO(a.registrationNumber.number, CAST(COALESCE(SUM(f.route.estimatedFlightTime.durationMinutes), 0) AS long)) " +
            "FROM Aircraft a LEFT JOIN ScheduledFlight f ON f.aircraftRegistration = a.registrationNumber.number " +
            "GROUP BY a.registrationNumber.number")
    Page<AircraftOperationalHoursDTO> findOperationalHoursPerAircraft(Pageable pageable);
}