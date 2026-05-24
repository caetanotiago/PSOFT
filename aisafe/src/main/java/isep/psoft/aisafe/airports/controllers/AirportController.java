package isep.psoft.aisafe.airports.controllers;

import isep.psoft.aisafe.airports.dto.*;
import isep.psoft.aisafe.airports.domain.Airport;
import isep.psoft.aisafe.airports.domain.Runway;
import isep.psoft.aisafe.airports.services.AddCertificationUseCase;
import isep.psoft.aisafe.airports.services.RegisterAirportUseCase;
import isep.psoft.aisafe.airports.services.SearchAirportsUseCase;
import isep.psoft.aisafe.airports.services.UpdateAirportStatusUseCase;
import isep.psoft.aisafe.airports.services.ViewAirportDetailsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/airports")
@Tag(name = "Airports", description = "Airport management — WP#2A")
public class AirportController {

    private final RegisterAirportUseCase registerAirportUseCase;
    private final ViewAirportDetailsUseCase viewAirportDetailsUseCase;
    private final SearchAirportsUseCase searchAirportsUseCase;
    private final AddCertificationUseCase addCertificationUseCase;
    private final UpdateAirportStatusUseCase updateAirportStatusUseCase;

    public AirportController(RegisterAirportUseCase registerAirportUseCase,
                             ViewAirportDetailsUseCase viewAirportDetailsUseCase,
                             SearchAirportsUseCase searchAirportsUseCase,
                             AddCertificationUseCase addCertificationUseCase,
                             UpdateAirportStatusUseCase updateAirportStatusUseCase) {
        this.registerAirportUseCase = registerAirportUseCase;
        this.viewAirportDetailsUseCase = viewAirportDetailsUseCase;
        this.searchAirportsUseCase = searchAirportsUseCase;
        this.addCertificationUseCase = addCertificationUseCase;
        this.updateAirportStatusUseCase = updateAirportStatusUseCase;
    }

    // ─── US106 — Register Airport ───────────────────────────────────────────────

    @PostMapping
    @PreAuthorize("hasRole('BACKOFFICE_OPERATOR')")
    @Operation(summary = "Register a new airport", description = "US106 — creates an airport with all Phase 1 data")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Airport registered"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "409", description = "IATA code already exists")
    })
    public ResponseEntity<EntityModel<AirportDetailsResponseDTO>> registerAirport(
            @Valid @RequestBody RegisterAirportRequest request) {

        List<Runway> runways = request.runways().stream()
                .map(r -> new Runway(r.name(), r.length(), r.orientation()))
                .collect(Collectors.toList());

        Airport airport = registerAirportUseCase.registerAirport(
                request.iataCode(), request.name(), request.city(), request.country(),
                request.region(), request.timezone(),
                request.latitude(), request.longitude(),
                runways);

        EntityModel<AirportDetailsResponseDTO> model = buildDetailModel(airport);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{iataCode}")
                .buildAndExpand(airport.getIataCode().getCode())
                .toUri();

        return ResponseEntity.created(location).body(model);
    }

    // ─── US107 — View Airport Details ────────────────────────────────────────────

    @GetMapping("/{iataCode}")
    @PreAuthorize("hasAnyRole('BACKOFFICE_OPERATOR', 'ATCC')")
    @Operation(summary = "View airport details by IATA code", description = "US107")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Airport found"),
        @ApiResponse(responseCode = "400", description = "Invalid IATA code format"),
        @ApiResponse(responseCode = "404", description = "Airport not found")
    })
    public ResponseEntity<EntityModel<AirportDetailsResponseDTO>> getAirportDetails(
            @PathVariable String iataCode) {

        Airport airport = viewAirportDetailsUseCase.getAirportByIataCode(iataCode.toUpperCase());
        return ResponseEntity.ok(buildDetailModel(airport));
    }

    // ─── US108 — Search Airports ─────────────────────────────────────────────────

    @GetMapping
    @PreAuthorize("hasAnyRole('BACKOFFICE_OPERATOR', 'ATCC')")
    @Operation(summary = "Search airports by optional criteria", description = "US108 — returns empty list if no matches, never 404")
    @ApiResponse(responseCode = "200", description = "List of matching airports (possibly empty)")
    public ResponseEntity<CollectionModel<EntityModel<AirportSummaryResponseDTO>>> searchAirports(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country) {

        List<EntityModel<AirportSummaryResponseDTO>> items = searchAirportsUseCase
                .searchAirports(name, city, country)
                .stream()
                .map(a -> EntityModel.of(
                        AirportSummaryResponseDTO.from(a),
                        linkTo(methodOn(AirportController.class).getAirportDetails(a.getIataCode().getCode())).withSelfRel()
                ))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<AirportSummaryResponseDTO>> collection = CollectionModel.of(
                items,
                linkTo(methodOn(AirportController.class).searchAirports(name, city, country)).withSelfRel()
        );

        return ResponseEntity.ok(collection);
    }

    // ─── US106a — Add Airport Certification ──────────────────────────────────────

    @PostMapping("/{iataCode}/certifications")
    @PreAuthorize("hasRole('BACKOFFICE_OPERATOR')")
    @Operation(summary = "Add aircraft model certification to an airport", description = "US106a — returns 200 with the updated Airport (no new sub-resource URL)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Certification added, updated Airport returned"),
        @ApiResponse(responseCode = "404", description = "Airport not found"),
        @ApiResponse(responseCode = "409", description = "Model already certified")
    })
    public ResponseEntity<EntityModel<AirportDetailsResponseDTO>> addCertification(
            @PathVariable String iataCode,
            @Valid @RequestBody AddCertificationRequest request) {

        Airport airport = addCertificationUseCase.addCertification(
                iataCode, request.manufacturer(), request.modelName());
        return ResponseEntity.ok(buildDetailModel(airport));
    }

    // ─── US109 — Update Airport Status ───────────────────────────────────────────

    @PatchMapping("/{iataCode}/status")
    @PreAuthorize("hasRole('BACKOFFICE_OPERATOR')")
    @Operation(summary = "Update airport operational status", description = "US109 — PATCH; @Version guarantees optimistic locking → 409 on conflict")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status updated"),
        @ApiResponse(responseCode = "400", description = "Invalid state value"),
        @ApiResponse(responseCode = "404", description = "Airport not found"),
        @ApiResponse(responseCode = "409", description = "Invalid transition or concurrent modification")
    })
    public ResponseEntity<EntityModel<AirportDetailsResponseDTO>> updateStatus(
            @PathVariable String iataCode,
            @Valid @RequestBody UpdateAirportStatusRequest request) {

        Airport airport = updateAirportStatusUseCase.updateStatus(iataCode, request.state());
        return ResponseEntity.ok(buildDetailModel(airport));
    }

    // ─── Helper ──────────────────────────────────────────────────────────────────

    private EntityModel<AirportDetailsResponseDTO> buildDetailModel(Airport airport) {
        String code = airport.getIataCode().getCode();
        return EntityModel.of(
                AirportDetailsResponseDTO.from(airport),
                linkTo(methodOn(AirportController.class).getAirportDetails(code)).withSelfRel(),
                linkTo(methodOn(AirportController.class).updateStatus(code, null)).withRel("update-status"),
                linkTo(methodOn(AirportController.class).addCertification(code, null)).withRel("add-certification"),
                linkTo(methodOn(AirportController.class).searchAirports(null, null, null)).withRel("search")
        );
    }
}
