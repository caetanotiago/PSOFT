package isep.psoft.aisafe.aircraftmanagement.repositories;

import isep.psoft.aisafe.aircraftmanagement.domain.Aircraft;
import isep.psoft.aisafe.aircraftmanagement.domain.RegistrationNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, RegistrationNumber>, JpaSpecificationExecutor<Aircraft> {
    
    // CORREÇÃO: Adicionar o método que faltava para que o UseCase possa usá-lo.
    // O Spring Data JPA irá gerar a implementação automaticamente.
    boolean existsByRegistrationNumber(RegistrationNumber registrationNumber);
}
