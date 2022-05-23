package io.codelex.flightplanner.service;

import io.codelex.flightplanner.database.AirportDatabaseRepository;
import io.codelex.flightplanner.database.FlightDatabaseRepository;
import io.codelex.flightplanner.modules.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@ConditionalOnProperty(prefix = "flight-planner", name = "store-type", havingValue = "database")
public class FlightsServiceDatabaseImpl extends AbstractFlightService implements FlightsService {

    private final FlightDatabaseRepository flightDatabaseRepository;
    private final AirportDatabaseRepository airportDatabaseRepository;

    public FlightsServiceDatabaseImpl(FlightDatabaseRepository flightDatabaseRepository,
                                      AirportDatabaseRepository airportDatabaseRepository) {
        this.flightDatabaseRepository = flightDatabaseRepository;
        this.airportDatabaseRepository = airportDatabaseRepository;
    }

    @Override
    public void clearFlight() {
        flightDatabaseRepository.deleteAll();
    }

    @Override
    public Flight addFlight(AddFlightRequest addFlightRequest) {

        Flight flight = new Flight(addFlightRequest);

        Optional<Flight> existingFlight = flightDatabaseRepository.findFlight(
                        flight.getFrom(),
                        flight.getTo(),
                        flight.getCarrier(),
                        flight.getDepartureTime(),
                        flight.getArrivalTime());

        if (existingFlight.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        if (sameFlightFromTo(flight) || !correctDateFormat(flight)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Optional<Airport> maybeAirportFrom = airportDatabaseRepository.findById(flight.getFrom().getAirport());
        Airport airportFrom = maybeAirportFrom.orElse(airportDatabaseRepository.save(flight.getFrom()));

        Optional<Airport> maybeAirportTo = airportDatabaseRepository.findById(flight.getTo().getAirport());
        Airport airportTo = maybeAirportTo.orElse(airportDatabaseRepository.save(flight.getTo()));

        flight.setFrom(airportFrom);
        flight.setTo(airportTo);

        return flightDatabaseRepository.save(flight);

    }

    @Override
    public void deleteFlight(int id) {
        flightDatabaseRepository
                .findById(id)
                .ifPresent(flightDatabaseRepository::delete);
    }

    @Override
    public Flight fetchFlight(int id) {
        return flightDatabaseRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Airport> searchAirports(String search) {

        String phrase = search.trim().toLowerCase();

        return airportDatabaseRepository.findByAllByPhrase(phrase);

    }

    @Override
    public PageResult<Flight> searchFlights(SearchFlightRequest searchFlightRequest) {

        if (sameSearchFlightFromTo(searchFlightRequest)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        List<Flight> flights = flightDatabaseRepository.findFlights(
                searchFlightRequest.getFrom(),
                searchFlightRequest.getTo(),
                searchFlightRequest.getDepartureDate().atStartOfDay(),
                searchFlightRequest.getDepartureDate().plusDays(1).atStartOfDay());

        return new PageResult<>(0, flights.size(), flights);
    }

}
