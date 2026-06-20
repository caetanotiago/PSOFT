package isep.psoft.aisafe.maintenance.repositories;

import isep.psoft.aisafe.maintenance.domain.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    // Método útil caso o ATCC queira ver apenas os alertas que ainda não leu
    List<Alert> findByIsReadFalse();
}