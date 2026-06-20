package isep.psoft.aisafe.airports.services;

import isep.psoft.aisafe.airports.domain.Airport;
import isep.psoft.aisafe.airports.domain.AirportGroup;
import isep.psoft.aisafe.airports.repositories.AirportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

// US211 - Visualizar aeroportos agrupados por região ou país.
// Apenas dois critérios fixos definidos pelo cliente (Conversation 011) — um simples
// if/else é suficiente; não se justifica um Strategy Pattern como em US214.
@Service
public class GroupAirportsUseCaseImpl implements GroupAirportsUseCase {

    private static final String UNSPECIFIED = "Unspecified";

    private final AirportRepository airportRepository;

    public GroupAirportsUseCaseImpl(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AirportGroup> groupBy(String criterion) {
        Function<Airport, String> keyExtractor = keyExtractorFor(criterion);

        return airportRepository.findAll().stream()
                .collect(Collectors.groupingBy(keyExtractor))
                .entrySet().stream()
                .sorted(Comparator.comparing(java.util.Map.Entry::getKey))
                .map(entry -> new AirportGroup(entry.getKey(), entry.getValue()))
                .toList();
    }

    private Function<Airport, String> keyExtractorFor(String criterion) {
        if (criterion == null)
            throw new IllegalArgumentException("'by' is required. Allowed values: region, country");

        return switch (criterion.toLowerCase()) {
            case "region" -> a -> blankToUnspecified(a.getDetails().getRegion());
            case "country" -> a -> blankToUnspecified(a.getDetails().getCountry());
            default -> throw new IllegalArgumentException(
                    "Invalid 'by' value: '" + criterion + "'. Allowed values: region, country");
        };
    }

    private String blankToUnspecified(String value) {
        return (value == null || value.isBlank()) ? UNSPECIFIED : value;
    }
}
