package isep.psoft.aisafe.maintenance.controllers;

import isep.psoft.aisafe.maintenance.domain.MaintenanceRecord;
import isep.psoft.aisafe.maintenance.dto.CreateRecordDTO;
import isep.psoft.aisafe.maintenance.services.MaintenanceRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maintenance-records")
public class MaintenanceRecordRestController {

    private final MaintenanceRecordService service;

    public MaintenanceRecordRestController(MaintenanceRecordService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MaintenanceRecord> createRecord(@RequestBody CreateRecordDTO dto) {
        MaintenanceRecord savedRecord = service.createRecord(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecord);
    }
}