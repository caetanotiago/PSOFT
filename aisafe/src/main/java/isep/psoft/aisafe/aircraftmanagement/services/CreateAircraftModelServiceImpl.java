package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.domain.AircraftModel;
import isep.psoft.aisafe.aircraftmanagement.domain.AircraftModelAlreadyExistsException;
import isep.psoft.aisafe.aircraftmanagement.domain.ModelDesignation;
import isep.psoft.aisafe.aircraftmanagement.domain.ModelImage;
import isep.psoft.aisafe.aircraftmanagement.domain.ModelSpecifications;
import isep.psoft.aisafe.aircraftmanagement.dto.CreateAircraftModelDTO;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftModelRepository;
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
        // CORRIGIDO: usa exceção de domínio (-> 409 via GlobalExceptionHandler)
        // em vez de IllegalArgumentException (-> 400), porque isto é um conflito
        // de recurso duplicado, não um erro de validação de input.
        if (aircraftModelRepository.findByDesignationModelName(dto.getModelName()).isPresent()) {
            throw new AircraftModelAlreadyExistsException(dto.getModelName());
        }

        // 1. Cria os Value Objects (validações ocorrem nos construtores)
        ModelDesignation designation = new ModelDesignation(dto.getManufacturer(), dto.getModelName());

        ModelSpecifications specifications = new ModelSpecifications(
                dto.getStandardCapacity(), dto.getFuelCapacity(),
                dto.getMaximumRange(), dto.getCruisingSpeed());

        // 2. CORRIGIDO (US202): trata a imagem opcional
        AircraftModel newModel;
        if (dto.getImage() != null && !dto.getImage().isBlank()) {
            ModelImage image = new ModelImage(dto.getImage());
            newModel = new AircraftModel(designation, specifications, image);
        } else {
            newModel = new AircraftModel(designation, specifications);
        }

        // 3. Persiste na Base de Dados e retorna
        // NOTA: a constraint UNIQUE de modelName na BD apanha a race condition
        // residual (dois pedidos concorrentes com o mesmo nome); nesse caso o save()
        // lança DataIntegrityViolationException, tratada no GlobalExceptionHandler (-> 409).
        return aircraftModelRepository.save(newModel);
    }
}