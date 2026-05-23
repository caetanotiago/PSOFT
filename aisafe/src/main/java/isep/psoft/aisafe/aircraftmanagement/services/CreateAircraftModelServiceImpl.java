package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.domain.AircraftModel;
import isep.psoft.aisafe.aircraftmanagement.domain.ModelDesignation;
import isep.psoft.aisafe.aircraftmanagement.domain.ModelSpecifications;
import isep.psoft.aisafe.aircraftmanagement.dto.CreateAircraftModelDTO;
import org.springframework.stereotype.Service;

@Service
public class CreateAircraftModelServiceImpl implements CreateAircraftModelService {

    private final AircraftModelRepository aircraftModelRepository;

    public CreateAircraftModelServiceImpl(AircraftModelRepository aircraftModelRepository) {
        this.aircraftModelRepository = aircraftModelRepository;
    }

    @Override
    public AircraftModel createAircraftModel(CreateAircraftModelDTO dto) {
        // Verifica se já existe um modelo com o mesmo nome
        if (aircraftModelRepository.findByDesignationModelName(dto.getModelName()).isPresent()) {
            throw new IllegalArgumentException("An aircraft model with the name '" + dto.getModelName() + "' already exists.");
        }

        // 1. Cria os Value Objects (validações ocorrem nos construtores)
        ModelDesignation designation = new ModelDesignation(dto.getManufacturer(), dto.getModelName());
        
        ModelSpecifications specifications = new ModelSpecifications(
                dto.getStandardCapacity(), dto.getFuelCapacity(), 
                dto.getMaximumRange(), dto.getCruisingSpeed());

        // 2. Instancia a Entidade Raiz
        AircraftModel newModel = new AircraftModel(designation, specifications);

        // 3. Persiste na Base de Dados e retorna
        return aircraftModelRepository.save(newModel);
    }
}