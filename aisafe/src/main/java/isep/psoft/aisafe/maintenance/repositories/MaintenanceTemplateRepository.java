package isep.psoft.aisafe.Maintenance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isep.psoft.aisafe.Maintenance.domain.MaintenanceTemplate;

import java.util.Optional;

@Repository
public interface MaintenanceTemplateRepository extends JpaRepository<MaintenanceTemplate, Long> {
    // Método para garantir que não criamos templates com nomes repetidos
    Optional<MaintenanceTemplate> findByTemplateName(String templateName);
}