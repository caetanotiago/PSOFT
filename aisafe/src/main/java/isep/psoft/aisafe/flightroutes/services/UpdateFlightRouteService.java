package isep.psoft.aisafe.flightroutes.services;

import isep.psoft.aisafe.flightroutes.domain.EstimatedFlightTime;
import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
import isep.psoft.aisafe.flightroutes.domain.RouteRequirements;
import isep.psoft.aisafe.flightroutes.dto.UpdateRouteDTO;
import isep.psoft.aisafe.flightroutes.repositories.FlightRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateFlightRouteService {

    private final FlightRouteRepository routeRepository;

    @Transactional
    public FlightRoute updateRoute(String id, UpdateRouteDTO dto) {
        
        // Procurar a Rota
        FlightRoute route = routeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Flight Route not found: " + id));

        // Verificar se é para desativar
        if ("INACTIVE".equalsIgnoreCase(dto.getStatus())) {
            route.deactivate();
        } 
        // Ou se é para atualizar detalhes operacionais
        else if (dto.getMinRange() != null && dto.getMinCapacity() != null && dto.getEstimatedFlightTime() != null) {
            RouteRequirements newReqs = new RouteRequirements(dto.getMinRange(), dto.getMinCapacity());
            EstimatedFlightTime newTime = new EstimatedFlightTime(dto.getEstimatedFlightTime());
            route.updateDetails(newReqs, newTime);
        }

        
        return routeRepository.save(route);
    }
}