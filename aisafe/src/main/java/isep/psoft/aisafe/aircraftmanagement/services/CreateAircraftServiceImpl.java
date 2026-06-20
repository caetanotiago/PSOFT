package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.domain.*;
import isep.psoft.aisafe.aircraftmanagement.dto.CreateAircraftDTO;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftModelRepository;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateAircraftServiceImpl implements CreateAircraftService {


    private final AircraftRepository aircraftRepository;
    private final AircraftModelRepository aircraftModelRepository;

    public CreateAircraftServiceImpl(AircraftRepository aircraftRepository,
                                     AircraftModelRepository aircraftModelRepository) {
        this.aircraftRepository = aircraftRepository;
        this.aircraftModelRepository = aircraftModelRepository;
    }

    @Override
    public Aircraft createAircraft(CreateAircraftDTO dto) {
        RegistrationNumber regNum = new RegistrationNumber(dto.getRegistrationNumber());

        // 1. Validar se a matrícula já existe
        if (aircraftRepository.existsByRegistrationNumber(regNum)) {
            throw new AircraftAlreadyExistsException(dto.getRegistrationNumber());
        }

        // 2. Validar se o Aircraft Model existe
        AircraftModel model = aircraftModelRepository.findByDesignationModelName(dto.getModelName())
                .orElseThrow(() -> new AircraftModelNotFoundException(dto.getModelName()));

        // 3. Criar os Value Objects e instanciar o Aircraft
        ManufacturingDate mfgDate = new ManufacturingDate(dto.getManufacturingDate());
        SeatingCapacity capacity = new SeatingCapacity(dto.getSeatingCapacity());
        AircraftStatus status = new AircraftStatus(dto.getStatus());

        Aircraft aircraft = new Aircraft(regNum, model, mfgDate, capacity, status);
        return aircraftRepository.save(aircraft);
    }
}