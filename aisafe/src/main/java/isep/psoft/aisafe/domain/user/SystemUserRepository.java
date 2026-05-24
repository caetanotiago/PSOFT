package isep.psoft.aisafe.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Slide "Repositories: Where They Live" (7_REST_Web_API_DDD_Library):
// A interface do repositório pertence ao domínio.
public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {

    Optional<SystemUser> findByUsername(String username);
}
