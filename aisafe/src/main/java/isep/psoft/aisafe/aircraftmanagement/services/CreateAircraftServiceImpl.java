package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.domain.*;
import isep.psoft.aisafe.aircraftmanagement.dto.CreateAircraftDTO;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftRepository;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftModelRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateAircraftServiceImpl implements CreateAircraftService {

    private final JpaRepository<Aircraft, RegistrationNumber> aircraftRepository;
    private final AircraftModelRepository aircraftModelRepository;

    public CreateAircraftServiceImpl(JpaRepository<Aircraft, RegistrationNumber> aircraftRepository, AircraftModelRepository aircraftModelRepository) {
        this.aircraftRepository = aircraftRepository;
        this.aircraftModelRepository = aircraftModelRepository;
    }

    @Override
    public Aircraft createAircraft(CreateAircraftDTO dto) {
        RegistrationNumber regNum = new RegistrationNumber(dto.getRegistrationNumber());
        
        // 1. Validar se a matrícula já existe
        if (aircraftRepository.findById(regNum).isPresent()) {
            throw new IllegalArgumentException("Aircraft with registration number '" + dto.getRegistrationNumber() + "' already exists.");
        }

        // 2. Validar se o Aircraft Model existe
        AircraftModel model = aircraftModelRepository.findByDesignationModelName(dto.getModelName())
                .orElseThrow(() -> new IllegalArgumentException("Aircraft Model '" + dto.getModelName() + "' not found."));

        // 3. Criar os Value Objects e instanciar o Aircraft
        ManufacturingDate mfgDate = new ManufacturingDate(dto.getManufacturingDate());
        SeatingCapacity capacity = new SeatingCapacity(dto.getSeatingCapacity());
        AircraftStatus status = new AircraftStatus(dto.getStatus());

        Aircraft aircraft = new Aircraft(regNum, model, mfgDate, capacity, status);
        return aircraftRepository.save(aircraft);
    }
}
