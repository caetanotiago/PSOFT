package isep.psoft.aisafe.maintenance.controllers;

import isep.psoft.aisafe.maintenance.dto.CompleteRecordInputDto;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import isep.psoft.aisafe.maintenance.dto.TotalMaintenanceHoursDto;
import isep.psoft.aisafe.maintenance.services.common.ViewMaintenanceRecordByIdUseCase;
import isep.psoft.aisafe.maintenance.services.us116.ViewAircraftMaintenanceRecordsUseCase;
import isep.psoft.aisafe.maintenance.services.us117.ViewTotalMaintenanceHoursUseCase;
import isep.psoft.aisafe.maintenance.services.us119.CompleteMaintenanceRecordUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance-records")
public class MaintenanceController {

    @Autowired
    private ViewAircraftMaintenanceRecordsUseCase viewRecordsUseCase;

    @Autowired
    private ViewTotalMaintenanceHoursUseCase viewTotalHoursUseCase;

    @Autowired
    private CompleteMaintenanceRecordUseCase completeRecordUseCase;

    @Autowired
    private ViewMaintenanceRecordByIdUseCase viewByIdUseCase;

    /**
     * US116: View Maintenance Records of a Specific Aircraft
     */
    @GetMapping("/aircraft/{registration}")
    //@PreAuthorize("hasRole('MAINTENANCE_MANAGER')")
    public ResponseEntity<List<MaintenanceRecordOutputDto>> getRecordsByAircraft(@PathVariable String registration) {
        List<MaintenanceRecordOutputDto> records = viewRecordsUseCase.execute(registration);
        return ResponseEntity.ok(records);
    }

    /**
     * US117: View Total Maintenance Hours for the Fleet
     */
    @GetMapping("/fleet/maintenance-hours")
    //@PreAuthorize("hasRole('FLEET_MANAGER')")
    public ResponseEntity<TotalMaintenanceHoursDto> getTotalMaintenanceHours() {
        TotalMaintenanceHoursDto totalHours = viewTotalHoursUseCase.execute();
        return ResponseEntity.ok(totalHours);
    }

    /**
     * US119: Complete a Maintenance Record
     */
    @PatchMapping("/{id}/complete")
    //@PreAuthorize("hasRole('MAINTENANCE_TECHNICIAN')")
    public ResponseEntity<MaintenanceRecordOutputDto> completeMaintenanceRecord(@PathVariable Long id,
                                                                                @RequestBody CompleteRecordInputDto dto,
                                                                                @RequestHeader("If-Match") String ifMatch) {
        MaintenanceRecordOutputDto updatedRecord = completeRecordUseCase.execute(id, dto, ifMatch);
        return ResponseEntity.ok(updatedRecord);
    }

    /**
     * Endpoint to get a single maintenance record by its ID.
     * Used for HATEOAS self links.
     */
    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('MAINTENANCE_MANAGER') or hasRole('MAINTENANCE_TECHNICIAN')")
    public ResponseEntity<MaintenanceRecordOutputDto> getRecordById(@PathVariable Long id) {
        return viewByIdUseCase.execute(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
