package isep.psoft.aisafe.airports.repositories;

import isep.psoft.aisafe.airports.domain.Airport;
import isep.psoft.aisafe.airports.domain.IATACode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Slide "Repositories: Where They Live" (7_REST_Web_API_DDD_Library):
// A interface do repositório pertence ao domínio; Spring Data gera a implementação em runtime.
public interface AirportRepository extends JpaRepository<Airport, IATACode> {

    @Query("SELECT a FROM Airport a WHERE " +
           "(:name IS NULL OR LOWER(a.details.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:city IS NULL OR LOWER(a.details.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
           "(:country IS NULL OR LOWER(a.details.country) LIKE LOWER(CONCAT('%', :country, '%')))")
    List<Airport> searchByCriteria(@Param("name") String name,
                                   @Param("city") String city,
                                   @Param("country") String country);
}
