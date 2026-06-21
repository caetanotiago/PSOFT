package isep.psoft.aisafe.maintenance.repositories;

import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.dto.CostItemDto;
import isep.psoft.aisafe.maintenance.dto.TurnaroundItemDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {

    /**
     * US116: Finds all maintenance records for a specific aircraft registration.
     */
    List<MaintenanceRecord> findAllByAircraftRegistration(String aircraftRegistration);

    /**
     * US117: Calculates the sum of all expected maintenance durations in minutes.
     */
    @Query("SELECT COALESCE(SUM(mr.recordDetails.expectedDurationMinutes), 0) FROM MaintenanceRecord mr")
    Long sumTotalExpectedDurationMinutes();

    /**
     * US218: Search Maintenance Records by optional filters.
     */
    @Query("SELECT m FROM MaintenanceRecord m WHERE " +
            "(:aircraftRegistration IS NULL OR m.aircraftRegistration = :aircraftRegistration) AND " +
            "(:componentCategory IS NULL OR m.component.category = :componentCategory) AND " +
            "(:status IS NULL OR (:status = 'ONGOING' AND m.completionNotes IS NULL) OR (:status = 'COMPLETED' AND m.completionNotes IS NOT NULL))")
    List<MaintenanceRecord> searchRecords(
            @Param("aircraftRegistration") String aircraftRegistration,
            @Param("componentCategory") String componentCategory,
            @Param("status") String status
    );

    /**
     * US219: Finds all ONGOING maintenance records (i.e., those without completion notes).
     */
    List<MaintenanceRecord> findByCompletionNotesIsNull();

    /**
     * US220: Calculate total maintenance costs grouped by Aircraft Registration.
     */
    @Query("SELECT new isep.psoft.aisafe.maintenance.dto.CostItemDto(m.aircraftRegistration, SUM(m.completionNotes.cost)) " +
            "FROM MaintenanceRecord m " +
            "WHERE m.completionNotes IS NOT NULL " +
            "GROUP BY m.aircraftRegistration")
    List<CostItemDto> calculateCostsPerAircraft();

    /**
     * US220: Calculate total maintenance costs grouped by Aircraft Model.
     */
    @Query("SELECT new isep.psoft.aisafe.maintenance.dto.CostItemDto(a.model.designation.modelName, SUM(m.completionNotes.cost)) " +
            "FROM MaintenanceRecord m, Aircraft a " +
            "WHERE m.aircraftRegistration = a.registrationNumber.number " +
            "AND m.completionNotes IS NOT NULL " +
            "GROUP BY a.model.designation.modelName")
    List<CostItemDto> calculateCostsPerModel();

    /**
     * US221: Calculate average maintenance turnaround time grouped by Aircraft Model.
     */
    @Query("SELECT new isep.psoft.aisafe.maintenance.dto.TurnaroundItemDto(a.model.designation.modelName, AVG(m.completionNotes.actualDurationMinutes)) " +
            "FROM MaintenanceRecord m, Aircraft a " +
            "WHERE m.aircraftRegistration = a.registrationNumber.number " +
            "AND m.completionNotes IS NOT NULL " +
            "GROUP BY a.model.designation.modelName")
    List<TurnaroundItemDto> findAverageTurnaroundTimePerModel();

    /**
     * US222: Encontra o último registo de manutenção concluído para um avião e template específicos.
     * O Spring Data JPA traduz este nome gigante automaticamente para uma query SQL!
     */
    Optional<MaintenanceRecord> findTopByAircraftRegistrationAndMaintenanceTemplateIdAndCompletionNotesIsNotNullOrderByCompletionNotes_CompletionDateDesc(
            String aircraftRegistration,
            Long templateId
    );

}