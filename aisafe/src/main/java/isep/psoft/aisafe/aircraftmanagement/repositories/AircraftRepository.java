package isep.psoft.aisafe.aircraftmanagement.repositories;

import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;
import isep.psoft.aisafe.aircraftmanagement.domain.RegistrationNumber;
import isep.psoft.aisafe.aircraftmanagement.dto.AircraftOperationalHoursDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, RegistrationNumber>, JpaSpecificationExecutor<Aircraft> {

    boolean existsByRegistrationNumber(RegistrationNumber registrationNumber);

    @Query("SELECT new isep.psoft.aisafe.aircraftmanagement.dto.AircraftOperationalHoursDTO(" +
           "a.registrationNumber.number, COALESCE(SUM(sf.route.estimatedFlightTime.durationMinutes), 0)) " +
           "FROM Aircraft a LEFT JOIN ScheduledFlight sf ON sf.aircraftRegistration = a.registrationNumber.number " +
           "GROUP BY a.registrationNumber.number")
    Page<AircraftOperationalHoursDTO> findOperationalHours(Pageable pageable);
}