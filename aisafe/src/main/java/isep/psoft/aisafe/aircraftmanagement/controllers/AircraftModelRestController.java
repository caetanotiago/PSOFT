package isep.psoft.aisafe.aircraftmanagement.controllers;

import isep.psoft.aisafe.aircraftmanagement.assemblers.AircraftModelAssembler;
import isep.psoft.aisafe.aircraftmanagement.domain.AircraftModel;
import isep.psoft.aisafe.aircraftmanagement.dto.AircraftModelDTO;
import isep.psoft.aisafe.aircraftmanagement.dto.AircraftModelUtilizationDTO;
import isep.psoft.aisafe.aircraftmanagement.dto.CreateAircraftModelDTO;
import isep.psoft.aisafe.aircraftmanagement.dto.UpdateModelSpecificationsDTO;
import isep.psoft.aisafe.aircraftmanagement.services.CreateAircraftModelService;
import isep.psoft.aisafe.aircraftmanagement.services.UpdateAircraftModelService;
import isep.psoft.aisafe.aircraftmanagement.services.ViewTopUtilizedModelsService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aircraft-models")
public class AircraftModelRestController {

    private final CreateAircraftModelService createService;
    private final UpdateAircraftModelService updateService;
    private final ViewTopUtilizedModelsService viewTopUtilizedModelsService;
    private final AircraftModelAssembler assembler;

    public AircraftModelRestController(CreateAircraftModelService createService,
                                       UpdateAircraftModelService updateService,
                                       ViewTopUtilizedModelsService viewTopUtilizedModelsService,
                                       AircraftModelAssembler assembler) {
        this.createService = createService;
        this.updateService = updateService;
        this.viewTopUtilizedModelsService = viewTopUtilizedModelsService;
        this.assembler = assembler;
    }

    // US101 + US202 — criar modelo (com imagem opcional)
    @PostMapping
    @RolesAllowed("ROLE_BACKOFFICE_OPERATOR")
    public ResponseEntity<EntityModel<AircraftModelDTO>> createAircraftModel(
            @RequestBody CreateAircraftModelDTO dto) {
        AircraftModel saved = createService.createAircraftModel(dto);
        return new ResponseEntity<>(assembler.toModel(saved), HttpStatus.CREATED);
    }

    // GET necessário para o self-link do HATEOAS funcionar
    @GetMapping("/{modelName}")
    @RolesAllowed({"ROLE_BACKOFFICE_OPERATOR", "ROLE_ATCC"})
    public ResponseEntity<EntityModel<AircraftModelDTO>> getAircraftModel(
            @PathVariable String modelName) {
        AircraftModel model = updateService.findByModelName(modelName);
        return ResponseEntity.ok(assembler.toModel(model));
    }

    // US201 — atualizar specs com Optimistic Locking (If-Match header)
    @PatchMapping("/{modelName}/specifications")
    @RolesAllowed("ROLE_BACKOFFICE_OPERATOR")
    public ResponseEntity<EntityModel<AircraftModelDTO>> updateSpecifications(
            @PathVariable String modelName,
            @RequestHeader("If-Match") Long version,
            @RequestBody UpdateModelSpecificationsDTO dto) {
        AircraftModel updated = updateService.updateSpecifications(modelName, version, dto);
        return ResponseEntity.ok(assembler.toModel(updated));
    }

    // US204 — Top 5 modelos mais utilizados
    @GetMapping("/top-utilized")
    @RolesAllowed("ROLE_BACKOFFICE_OPERATOR")
    public ResponseEntity<List<AircraftModelUtilizationDTO>> getTopUtilizedModels(
            @RequestParam(defaultValue = "assignments") String metric) {
        List<AircraftModelUtilizationDTO> result = viewTopUtilizedModelsService
                .getTopUtilizedModels(metric);
        return ResponseEntity.ok(result);
    }
}