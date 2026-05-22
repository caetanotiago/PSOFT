package isep.psoft.aisafe.flightroutes.repositories;

import isep.psoft.aisafe.flightroutes.domain.FlightRoute;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRouteRepository extends CrudRepository<FlightRoute, String> {

    // US114 - Procurar por Origem e Destino
    @Query("SELECT r FROM FlightRoute r WHERE r.origin.iataCode.code = :origin AND r.destination.iataCode.code = :dest")
    List<FlightRoute> findByOriginAndDestination(@Param("origin") String origin, @Param("dest") String dest);

    // US114 e US113 (Extra) - Procurar apenas por Origem
    @Query("SELECT r FROM FlightRoute r WHERE r.origin.iataCode.code = :origin")
    List<FlightRoute> findByOrigin(@Param("origin") String origin);

    // US114 - Procurar apenas por Destino
    @Query("SELECT r FROM FlightRoute r WHERE r.destination.iataCode.code = :dest")
    List<FlightRoute> findByDestination(@Param("dest") String dest);

    // Útil para validações na criação de rotas (evitar rotas duplicadas)
    @Query("SELECT COUNT(r) > 0 FROM FlightRoute r WHERE r.origin.iataCode.code = :origin AND r.destination.iataCode.code = :dest")
    boolean existsByOriginAndDestination(@Param("origin") String origin, @Param("dest") String dest);
}