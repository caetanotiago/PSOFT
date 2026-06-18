package isep.psoft.aisafe.maintenance.controllers;

import isep.psoft.aisafe.maintenance.dto.CompleteRecordInputDto;
import isep.psoft.aisafe.maintenance.dto.CreateRecordDTO;
import isep.psoft.aisafe.maintenance.dto.MaintenanceRecordOutputDto;
import isep.psoft.aisafe.maintenance.dto.TotalMaintenanceHoursDto;
import isep.psoft.aisafe.maintenance.services.common.ViewMaintenanceRecordByIdUseCase;
import isep.psoft.aisafe.maintenance.services.us115a.CreateMaintenanceRecordUseCase;
import isep.psoft.aisafe.maintenance.services.us116.ViewAircraftMaintenanceRecordsUseCase;
import isep.psoft.aisafe.maintenance.services.us117.ViewTotalMaintenanceHoursUseCase;
import isep.psoft.aisafe.maintenance.services.us119.CompleteMaintenanceRecordUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance-records")
public class MaintenanceController {

    @Autowired private CreateMaintenanceRecordUseCase createRecordUseCase;
    @Autowired private ViewAircraftMaintenanceRecordsUseCase viewRecordsUseCase;
    @Autowired private ViewTotalMaintenanceHoursUseCase viewTotalHoursUseCase;
    @Autowired private CompleteMaintenanceRecordUseCase completeRecordUseCase;
    @Autowired private ViewMaintenanceRecordByIdUseCase viewByIdUseCase;

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
    // CORREÇÃO: O teu Bootstrapper não cria nenhum MANAGER. Tem de ser SUPERVISOR ou TECHNICIAN.
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
                                                                                @RequestHeader("If-Match") String ifMatch) {
        MaintenanceRecordOutputDto updatedRecord = completeRecordUseCase.execute(id, dto, ifMatch);
        return ResponseEntity.ok(updatedRecord);
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