package io.codelex.flightplanner.database;

import io.codelex.flightplanner.modules.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AirportDatabaseRepository extends JpaRepository<Airport, String> {

    @Query(value = "select a from Airport a where UPPER(a.airport) LIKE UPPER(CONCAT('%', :search, '%')) "
            + "or UPPER(a.country) LIKE UPPER(CONCAT('%', :search, '%')) "
            + "or UPPER(a.city) LIKE UPPER(CONCAT('%', :search, '%'))")
    List<Airport> findByAllByPhrase(String search);
}
