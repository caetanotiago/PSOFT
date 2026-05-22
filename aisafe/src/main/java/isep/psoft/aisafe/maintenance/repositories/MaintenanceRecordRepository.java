package isep.psoft.aisafe.maintenance.repositories;

import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {

    /**
     * US116: Finds all maintenance records for a specific aircraft registration.
     */
    List<MaintenanceRecord> findAllByAircraftRegistration(String aircraftRegistration);

    /**
     * US117: Calculates the sum of all expected maintenance durations in minutes.
     * The query targets the 'expectedDurationMinutes' field within the 'recordDetails' embedded object.
     * Returns 0 if there are no records.
     */
    @Query("SELECT COALESCE(SUM(mr.recordDetails.expectedDurationMinutes), 0) FROM MaintenanceRecord mr")
    Long sumTotalExpectedDurationMinutes();
}
