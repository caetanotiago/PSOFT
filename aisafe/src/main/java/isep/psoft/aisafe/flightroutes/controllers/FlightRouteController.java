package isep.psoft.aisafe.flightroutes.controllers;

import isep.psoft.aisafe.flightroutes.assemblers.FlightRouteAssembler;
import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
import isep.psoft.aisafe.flightroutes.domain.RouteHistory;
import isep.psoft.aisafe.flightroutes.dto.CreateRouteDTO;
import isep.psoft.aisafe.flightroutes.dto.FlightRouteDTO;
import isep.psoft.aisafe.flightroutes.dto.RouteHistoryDTO;
import isep.psoft.aisafe.flightroutes.dto.UpdateRouteDTO;
import isep.psoft.aisafe.flightroutes.services.CreateFlightRouteService;
import isep.psoft.aisafe.flightroutes.services.GetRouteHistoryService;
import isep.psoft.aisafe.flightroutes.services.SearchFlightRoutesService;
import isep.psoft.aisafe.flightroutes.services.UpdateFlightRouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class FlightRouteController {

    private final CreateFlightRouteService createService;
    private final UpdateFlightRouteService updateService;
    private final GetRouteHistoryService historyService;
    private final SearchFlightRoutesService searchService;
    
    private final FlightRouteAssembler assembler;

    // US110: Criar Rota
    @PostMapping
    public ResponseEntity<FlightRouteDTO> createRoute(@Valid @RequestBody CreateRouteDTO dto) {
        FlightRoute newRoute = createService.createRoute(dto);
        FlightRouteDTO responseDTO = assembler.toDTO(newRoute);
        
        // Retorna 201 Created 
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    // US113: Consultar detalhes de uma rota pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<FlightRouteDTO> getRouteById(@PathVariable String id) {
        FlightRoute route = searchService.getRouteById(id);
        return ResponseEntity.ok(assembler.toDTO(route));
    }

    // US114: Pesquisar rotas (Query params opcionais)
    @GetMapping("/search")
    public ResponseEntity<List<FlightRouteDTO>> searchRoutes(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String dest) {
        
        List<FlightRoute> routes = searchService.searchRoutes(origin, dest);
        return ResponseEntity.ok(assembler.toDTOList(routes));
    }

    // US112: Atualizar ou Desativar uma rota
    @PatchMapping("/{id}")
    public ResponseEntity<FlightRouteDTO> updateRoute(
            @PathVariable String id, 
            @Valid @RequestBody UpdateRouteDTO dto) {
        
        FlightRoute updatedRoute = updateService.updateRoute(id, dto);
        return ResponseEntity.ok(assembler.toDTO(updatedRoute));
    }

    // US111: Consultar histórico de uma rota
    @GetMapping("/{id}/history")
    public ResponseEntity<List<RouteHistoryDTO>> getRouteHistory(@PathVariable String id) {
        List<RouteHistory> historyLog = historyService.getRouteHistory(id);
        return ResponseEntity.ok(assembler.toHistoryDTOList(historyLog));
    }
}