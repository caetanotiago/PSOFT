package isep.psoft.aisafe.maintenance.controllers;

import isep.psoft.aisafe.maintenance.dto.CategorizeRecordInputDto;
import isep.psoft.aisafe.maintenance.dto.CompleteRecordInputDto;
import isep.psoft.aisafe.maintenance.dto.CreateRecordDTO;
import isep.psoft.aisafe.maintenance.dto.MaintenanceCostReportDto;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import isep.psoft.aisafe.maintenance.dto.TotalMaintenanceHoursDto;
import isep.psoft.aisafe.maintenance.dto.TurnaroundReportDto;
import isep.psoft.aisafe.maintenance.services.common.ViewMaintenanceRecordByIdUseCase;
import isep.psoft.aisafe.maintenance.services.us115a.CreateMaintenanceRecordUseCase;
import isep.psoft.aisafe.maintenance.services.us116.ViewAircraftMaintenanceRecordsUseCase;
import isep.psoft.aisafe.maintenance.services.us117.ViewTotalMaintenanceHoursUseCase;
import isep.psoft.aisafe.maintenance.services.us119.CompleteMaintenanceRecordUseCase;
import isep.psoft.aisafe.maintenance.services.us217.CategorizeMaintenanceRecordUseCase;
import isep.psoft.aisafe.maintenance.services.us218.SearchMaintenanceRecordsUseCase;
import isep.psoft.aisafe.maintenance.services.us219.ViewOngoingMaintenanceUseCase;
import isep.psoft.aisafe.maintenance.services.us220.GenerateCostReportUseCase;
import isep.psoft.aisafe.maintenance.services.us221.ViewAvgTurnaroundTimeUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance-records")
@RequiredArgsConstructor
public class MaintenanceController {

    private final CreateMaintenanceRecordUseCase createRecordUseCase;
    private final ViewAircraftMaintenanceRecordsUseCase viewRecordsUseCase;
    private final ViewTotalMaintenanceHoursUseCase viewTotalHoursUseCase;
    private final CompleteMaintenanceRecordUseCase completeRecordUseCase;
    private final ViewMaintenanceRecordByIdUseCase viewByIdUseCase;
    private final CategorizeMaintenanceRecordUseCase categorizeUseCase;
    private final SearchMaintenanceRecordsUseCase searchUseCase;
    private final ViewOngoingMaintenanceUseCase viewOngoingUseCase;
    private final GenerateCostReportUseCase generateCostReportUseCase;

    // Injeção do novo Use Case da US221
    private final ViewAvgTurnaroundTimeUseCase viewAvgTurnaroundTimeUseCase;

    /**
     * US115A: Create a Maintenance Record
     */
    @PostMapping
    @PreAuthorize("hasRole('MAINTENANCE_TECHNICIAN')")
    public ResponseEntity<MaintenanceRecordOutputDto> createRecord(@Valid @RequestBody CreateRecordDTO dto) {

        MaintenanceRecordOutputDto outputDto = createRecordUseCase.execute(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(outputDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(outputDto);
    }

    /**
     * US116: View Maintenance Records of a Specific Aircraft
     */
    @GetMapping("/aircraft/{registration}")
    @PreAuthorize("hasRole('MAINTENANCE_SUPERVISOR') or hasRole('MAINTENANCE_TECHNICIAN')")
    public ResponseEntity<List<MaintenanceRecordOutputDto>> getRecordsByAircraft(@PathVariable String registration) {
        List<MaintenanceRecordOutputDto> records = viewRecordsUseCase.execute(registration);
        return ResponseEntity.ok(records);
    }

    /**
     * US117: View Total Maintenance Hours for the Fleet
     */
    @GetMapping("/fleet/maintenance-hours")
    @PreAuthorize("hasRole('ATCC')")
    public ResponseEntity<TotalMaintenanceHoursDto> getTotalMaintenanceHours() {
        TotalMaintenanceHoursDto totalHours = viewTotalHoursUseCase.execute();
        return ResponseEntity.ok(totalHours);
    }

    /**
     * US119: Complete a Maintenance Record
     */
    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasRole('MAINTENANCE_TECHNICIAN')")
    public ResponseEntity<MaintenanceRecordOutputDto> completeMaintenanceRecord(@PathVariable Long id,
                                                                                @Valid @RequestBody CompleteRecordInputDto dto,
                                                                                @RequestHeader(value = "If-Match", required = false) String ifMatch) {
        MaintenanceRecordOutputDto updatedRecord = completeRecordUseCase.execute(id, dto, ifMatch);
        return ResponseEntity.ok(updatedRecord);
    }

    /**
     * US217: Categorize a Maintenance Record Component
     */
    @PatchMapping("/{id}/component")
    @PreAuthorize("hasRole('MAINTENANCE_TECHNICIAN') or hasRole('MAINTENANCE_SUPERVISOR')")
    public ResponseEntity<MaintenanceRecordOutputDto> categorizeComponent(
            @PathVariable Long id,
            @Valid @RequestBody CategorizeRecordInputDto inputDto,
            @RequestHeader(value = "If-Match", required = false) Long version) {

        MaintenanceRecordOutputDto updatedRecord = categorizeUseCase.execute(id, inputDto.getCategory(), version);

        return ResponseEntity.ok(updatedRecord);
    }

    /**
     * US218: Search Maintenance Records by optional filters
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ATCC')")
    public ResponseEntity<List<MaintenanceRecordOutputDto>> searchRecords(
            @RequestParam(required = false) String aircraft,
            @RequestParam(required = false) String component,
            @RequestParam(required = false) String status) {

        List<MaintenanceRecordOutputDto> records = searchUseCase.execute(aircraft, component, status);

        return ResponseEntity.ok(records);
    }

    /**
     * US219: View all ongoing maintenance activities in the fleet
     */
    @GetMapping("/ongoing")
    @PreAuthorize("hasRole('MAINTENANCE_SUPERVISOR')")
    public ResponseEntity<List<MaintenanceRecordOutputDto>> getOngoingMaintenance() {
        List<MaintenanceRecordOutputDto> records = viewOngoingUseCase.execute();
        return ResponseEntity.ok(records);
    }

    /**
     * US220: Generate reports on maintenance costs
     */
    @GetMapping("/reports/costs")
    @PreAuthorize("hasRole('ATCC')")
    public ResponseEntity<MaintenanceCostReportDto> getMaintenanceCostReport(
            @RequestParam(name = "type", required = true) String type) {

        MaintenanceCostReportDto report = generateCostReportUseCase.execute(type);
        return ResponseEntity.ok(report);
    }

    /**
     * US221: View average maintenance turnaround time per aircraft type
     */
    @GetMapping("/reports/turnaround-time")
    @PreAuthorize("hasRole('MAINTENANCE_SUPERVISOR')")
    public ResponseEntity<TurnaroundReportDto> getAverageTurnaroundTime() {

        TurnaroundReportDto report = viewAvgTurnaroundTimeUseCase.execute();
        return ResponseEntity.ok(report);
    }

    /**
     * Endpoint to get a single maintenance record by its ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MAINTENANCE_MANAGER') or hasRole('MAINTENANCE_TECHNICIAN')")
    public ResponseEntity<MaintenanceRecordOutputDto> getRecordById(@PathVariable Long id) {
        return viewByIdUseCase.execute(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}