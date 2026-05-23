package isep.psoft.aisafe.aircraftmanagement.controllers;

import isep.psoft.aisafe.aircraftmanagement.assemblers.AircraftAssembler;
import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;
import isep.psoft.aisafe.aircraftmanagement.dto.AircraftDTO;
import isep.psoft.aisafe.aircraftmanagement.dto.CreateAircraftDTO;
import isep.psoft.aisafe.aircraftmanagement.services.CreateAircraftService;
import isep.psoft.aisafe.aircraftmanagement.services.ViewAircraftService;
import isep.psoft.aisafe.aircraftmanagement.services.SearchAircraftService;
import isep.psoft.aisafe.aircraftmanagement.services.UpdateAircraftStatusService;
import isep.psoft.aisafe.aircraftmanagement.dto.UpdateAircraftStatusDTO;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/aircrafts")
public class AircraftRestController {

    private final CreateAircraftService createAircraftService;
    private final ViewAircraftService viewAircraftService;
    private final SearchAircraftService searchAircraftService;
    private final UpdateAircraftStatusService updateAircraftStatusService;
    private final AircraftAssembler assembler;

    public AircraftRestController(CreateAircraftService createAircraftService, ViewAircraftService viewAircraftService, SearchAircraftService searchAircraftService, UpdateAircraftStatusService updateAircraftStatusService, AircraftAssembler assembler) {
        this.createAircraftService = createAircraftService;
        this.viewAircraftService = viewAircraftService;
        this.searchAircraftService = searchAircraftService;
        this.updateAircraftStatusService = updateAircraftStatusService;
        this.assembler = assembler;
    }

    @PostMapping
    @RolesAllowed("ROLE_ATCC")
    public ResponseEntity<EntityModel<AircraftDTO>> createAircraft(@RequestBody CreateAircraftDTO dto) {
        Aircraft savedAircraft = createAircraftService.createAircraft(dto);
        return new ResponseEntity<>(assembler.toModel(savedAircraft), HttpStatus.CREATED);
    }

    @GetMapping("/{registrationNumber}")
    @RolesAllowed({"ROLE_ATCC", "ROLE_BACKOFFICE_OPERATOR"})
    public ResponseEntity<EntityModel<AircraftDTO>> getAircraft(@PathVariable String registrationNumber) {
        Aircraft aircraft = viewAircraftService.getAircraftByRegistrationNumber(registrationNumber);
        return ResponseEntity.ok(assembler.toModel(aircraft));
    }

    @GetMapping
    @RolesAllowed("ROLE_ATCC")
    public ResponseEntity<PagedModel<EntityModel<AircraftDTO>>> searchAircrafts(
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer year,
            Pageable pageable,
            PagedResourcesAssembler<Aircraft> pagedAssembler) {
        Page<Aircraft> aircraftPage = searchAircraftService.searchAircrafts(model, status, year, pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(aircraftPage, assembler));
    }

    @PatchMapping("/{registrationNumber}/status")
    @RolesAllowed("ROLE_ATCC")
    public ResponseEntity<EntityModel<AircraftDTO>> updateAircraftStatus(
            @PathVariable String registrationNumber,
            @RequestBody UpdateAircraftStatusDTO dto) {
        Aircraft updatedAircraft = updateAircraftStatusService.updateStatus(registrationNumber, dto.getStatus(), dto.getVersion());
        return ResponseEntity.ok(assembler.toModel(updatedAircraft));
    }
}