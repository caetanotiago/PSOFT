package isep.psoft.aisafe.aircraftmanagement.services;

import isep.psoft.aisafe.aircraftmanagement.domain.AircraftModel;
import isep.psoft.aisafe.aircraftmanagement.domain.AircraftModelNotFoundException;
import isep.psoft.aisafe.aircraftmanagement.domain.ModelSpecifications;
import isep.psoft.aisafe.aircraftmanagement.dto.UpdateModelSpecificationsDTO;
import isep.psoft.aisafe.aircraftmanagement.repositories.AircraftModelRepository;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateAircraftModelServiceImpl implements UpdateAircraftModelService {

    private final AircraftModelRepository repository;

    public UpdateAircraftModelServiceImpl(AircraftModelRepository repository) {
        this.repository = repository;
    }

    @Override
    public AircraftModel findByModelName(String modelName) {
        return repository.findByDesignationModelName(modelName)
                .orElseThrow(() -> new AircraftModelNotFoundException(modelName));
    }

    @Override
    @Transactional
    public AircraftModel updateSpecifications(String modelName, Long expectedVersion,
                                              UpdateModelSpecificationsDTO dto) {
        AircraftModel model = repository.findByDesignationModelName(modelName)
                .orElseThrow(() -> new AircraftModelNotFoundException(modelName));

        if (expectedVersion != null && !expectedVersion.equals(model.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(AircraftModel.class, modelName);
        }

        ModelSpecifications newSpecs = new ModelSpecifications(
                dto.getStandardCapacity(),
                dto.getFuelCapacity(),
                dto.getMaximumRange(),
                dto.getCruisingSpeed()
        );

        model.updateSpecifications(newSpecs);
        return repository.save(model);
    }
}