package isep.psoft.aisafe.maintenance.controllers;

import isep.psoft.aisafe.maintenance.dto.CreateTemplateDTO;
import isep.psoft.aisafe.maintenance.dto.TemplateDTO;
import isep.psoft.aisafe.maintenance.services.common.ViewMaintenanceTemplateByIdUseCase;
import isep.psoft.aisafe.maintenance.services.us115.CreateMaintenanceTemplateUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/maintenance-templates")
@RequiredArgsConstructor
public class MaintenanceTemplateRestController {

    private final CreateMaintenanceTemplateUseCase createUseCase;

    private final ViewMaintenanceTemplateByIdUseCase viewByIdUseCase;

    @PostMapping
    // CORREÇÃO: Passou para BACKOFFICE_OPERATOR para não chocar com o SecurityConfig
    @PreAuthorize("hasRole('BACKOFFICE_OPERATOR')")
    public ResponseEntity<TemplateDTO> createTemplate(@Valid @RequestBody CreateTemplateDTO createDto) {

        TemplateDTO responseDto = createUseCase.execute(createDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    @GetMapping("/{id}")
    // CORREÇÃO: Passou para BACKOFFICE_OPERATOR ou SUPERVISOR
    @PreAuthorize("hasRole('BACKOFFICE_OPERATOR') or hasRole('MAINTENANCE_SUPERVISOR')")
    public ResponseEntity<TemplateDTO> getTemplateById(@PathVariable Long id) {
        return viewByIdUseCase.execute(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}