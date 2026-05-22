package isep.psoft.aisafe.maintenance.controllers;

import isep.psoft.aisafe.maintenance.domain.MaintenanceTemplate;
import isep.psoft.aisafe.maintenance.dto.CreateTemplateDTO;
import isep.psoft.aisafe.maintenance.dto.TemplateDTO;
import isep.psoft.aisafe.maintenance.dto.TemplateAssembler;
import isep.psoft.aisafe.maintenance.services.MaintenanceTemplateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize; // Descomenta quando a equipa configurar a Segurança JWT
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maintenance-templates")
public class MaintenanceTemplateRestController {

    private final MaintenanceTemplateService service;
    private final TemplateAssembler assembler;

    public MaintenanceTemplateRestController(MaintenanceTemplateService service, TemplateAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @PostMapping
    // @PreAuthorize("hasRole('MAINTENANCE_TECHNICIAN')") // Exigência da US (AC4)
    public ResponseEntity<TemplateDTO> createTemplate(@RequestBody CreateTemplateDTO createDto) {

        // 1. O Serviço cria e guarda o template
        MaintenanceTemplate savedTemplate = service.createTemplate(createDto);

        // 2. O Assembler converte para DTO
        TemplateDTO responseDto = assembler.toDTO(savedTemplate);

        // 3. Devolvemos HTTP 201 Created com o JSON de resposta (Exigência AC5)
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}