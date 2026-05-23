package isep.psoft.aisafe.aircraftmanagement.controllers;

import isep.psoft.aisafe.aircraftmanagement.assemblers.AircraftModelAssembler;
import isep.psoft.aisafe.aircraftmanagement.domain.AircraftModel;
import isep.psoft.aisafe.aircraftmanagement.dto.AircraftModelDTO;
import isep.psoft.aisafe.aircraftmanagement.dto.CreateAircraftModelDTO;
import isep.psoft.aisafe.aircraftmanagement.services.CreateAircraftModelService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/aircraft-models")
public class AircraftModelRestController {

    private final CreateAircraftModelService service;
    private final AircraftModelAssembler assembler;

    public AircraftModelRestController(CreateAircraftModelService service, AircraftModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @PostMapping
    @RolesAllowed("ROLE_BACKOFFICE_OPERATOR")
    public ResponseEntity<AircraftModelDTO> createAircraftModel(@RequestBody CreateAircraftModelDTO dto) {
        AircraftModel savedModel = service.createAircraftModel(dto);
        return new ResponseEntity<>(assembler.toDTO(savedModel), HttpStatus.CREATED);
    }
}