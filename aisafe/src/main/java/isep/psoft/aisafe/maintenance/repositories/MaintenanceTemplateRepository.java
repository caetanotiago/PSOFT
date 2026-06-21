package isep.psoft.aisafe.maintenance.repositories;

import isep.psoft.aisafe.maintenance.domain.MaintenanceTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MaintenanceTemplateRepository extends JpaRepository<MaintenanceTemplate, Long> {

    Optional<MaintenanceTemplate> findByTemplateName(String templateName);
}