package isep.psoft.aisafe.maintenance.controllers;

import isep.psoft.aisafe.maintenance.dto.CreateTemplateDTO;
import isep.psoft.aisafe.maintenance.dto.TemplateDTO;
import isep.psoft.aisafe.maintenance.services.common.ViewMaintenanceTemplateByIdUseCase;
import isep.psoft.aisafe.maintenance.services.us115.CreateMaintenanceTemplateUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/maintenance-templates")
public class MaintenanceTemplateRestController {

    @Autowired
    private CreateMaintenanceTemplateUseCase createUseCase;

    @Autowired
    private ViewMaintenanceTemplateByIdUseCase viewByIdUseCase;

    @PostMapping
    //@PreAuthorize("hasRole('MAINTENANCE_TECHNICIAN')")
    public ResponseEntity<TemplateDTO> createTemplate(@RequestBody CreateTemplateDTO createDto) {
        // CORREÇÃO: O UseCase agora deve orquestrar tudo, incluindo a conversão para DTO.
        TemplateDTO responseDto = createUseCase.execute(createDto);
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('MAINTENANCE_TECHNICIAN')")
    public ResponseEntity<TemplateDTO> getTemplateById(@PathVariable Long id) {
        return viewByIdUseCase.execute(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
