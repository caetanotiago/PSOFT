package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;
import isep.psoft.aisafe.aircraftmanagement.domain.AircraftNotFoundException;
import isep.psoft.aisafe.aircraftmanagement.domain.RegistrationNumber;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import isep.psoft.aisafe.flightroutes.assemblers.FlightRouteAssembler;
import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
import isep.psoft.aisafe.flightroutes.dto.FlightRouteDTO;
import isep.psoft.aisafe.flightroutes.repositories.FlightRouteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewCompatibleRoutesServiceImpl implements ViewCompatibleRoutesService {

    private final AircraftRepository aircraftRepository;
    private final FlightRouteRepository flightRouteRepository;
    private final FlightRouteAssembler flightRouteAssembler;

    public ViewCompatibleRoutesServiceImpl(AircraftRepository aircraftRepository,
                                           FlightRouteRepository flightRouteRepository,
                                           FlightRouteAssembler flightRouteAssembler) {
        this.aircraftRepository = aircraftRepository;
        this.flightRouteRepository = flightRouteRepository;
        this.flightRouteAssembler = flightRouteAssembler;
    }

    @Override
    public Page<FlightRouteDTO> getCompatibleRoutes(String registrationNumber, Pageable pageable) {
        Aircraft aircraft = aircraftRepository.findByRegistration_Registration(registrationNumber)
                .orElseThrow(() -> new AircraftNotFoundException(registrationNumber));

        Double range = aircraft.getModel().getSpecifications().getMaximumRange();
        Integer capacity = aircraft.getSeatingCapacity().getTotalSeats();

        List<FlightRoute> compatible = flightRouteRepository.findCompatibleRoutes(range, capacity);
        List<FlightRouteDTO> dtos = flightRouteAssembler.toDTOList(compatible);

        // TODO(WP#3 dependency): paginação em memória — não cumpre o NFR6 da Fase 2
        // ("Long result lists must support pagination") porque carrega TODAS as rotas
        // compatíveis para RAM antes de paginar. Solução correta, pendente do colega
        // responsável pelo WP#3: alterar a assinatura do método no FlightRouteRepository
        // para aceitar Pageable e devolver Page<FlightRoute> diretamente da BD, ex:
        //   Page<FlightRoute> findCompatibleRoutes(Double range, Integer capacity, Pageable pageable);
        // Depois desta alteração, substituir os 4 blocos abaixo por uma única chamada:
        //   Page<FlightRoute> page = flightRouteRepository.findCompatibleRoutes(range, capacity, pageable);
        //   return page.map(flightRouteAssembler::toDTO);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), dtos.size());
        List<FlightRouteDTO> page = (start > dtos.size()) ? List.of() : dtos.subList(start, end);

        return new PageImpl<>(page, pageable, dtos.size());
    }
}