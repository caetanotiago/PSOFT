package isep.psoft.aisafe.maintenance.repositories;

import isep.psoft.aisafe.maintenance.domain.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    // Método útil caso o ATCC queira ver apenas os alertas que ainda não leu
    List<Alert> findByIsReadFalse();
}