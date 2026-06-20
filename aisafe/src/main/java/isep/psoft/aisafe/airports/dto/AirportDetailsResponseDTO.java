package isep.psoft.aisafe.airports.dto;

import isep.psoft.aisafe.airports.domain.Airport;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record AirportDetailsResponseDTO(
        String iataCode,
        String name,
        String city,
        String country,
        String region,
        String timezone,
        Double latitude,
        Double longitude,
        String status,
        List<RunwayDTO> runways,
        Set<ModelDesignationDTO> certifiedModels,
        List<FacilityDTO> facilities,
        List<PhotoDTO> photos,
        OperatingHoursDTO operatingHours,
        List<AirportContactDTO> contacts
) {
    public static AirportDetailsResponseDTO from(Airport airport) {
        return new AirportDetailsResponseDTO(
                airport.getIataCode().getCode(),           // IATACode VO → String para o DTO
                airport.getDetails().getName(),
                airport.getDetails().getCity(),
                airport.getDetails().getCountry(),
                airport.getDetails().getRegion(),
                airport.getDetails().getTimezone(),
                airport.getDetails().getCoordinates().getLatitude(),
                airport.getDetails().getCoordinates().getLongitude(),
                airport.getStatus().name(),                // AirportState diretamente (T9 Option A)
                airport.getRunways().stream().map(RunwayDTO::from).collect(Collectors.toList()),
                airport.getCertifiedModels().stream().map(ModelDesignationDTO::from).collect(Collectors.toSet()),
                airport.getFacilities().stream().map(FacilityDTO::from).collect(Collectors.toList()),
                airport.getPhotos().stream().map(PhotoDTO::from).collect(Collectors.toList()),
                airport.getOperatingHours() != null ? OperatingHoursDTO.from(airport.getOperatingHours()) : null,
                airport.getContacts().stream().map(AirportContactDTO::from).collect(Collectors.toList())
        );
    }
}
