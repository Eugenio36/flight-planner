package io.codelex.flightplanner.service;

import io.codelex.flightplanner.modules.*;
import io.codelex.flightplanner.repository.FlightsRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@ConditionalOnProperty(prefix = "flight-planner", name = "store-type", havingValue = "in-memory")
public class FlightsServiceInMemoryImpl extends AbstractFlightService implements FlightsService {

    private final FlightsRepository flightsRepository;

    public FlightsServiceInMemoryImpl(FlightsRepository flightsRepository) {
        this.flightsRepository = flightsRepository;
    }

    public Flight addFlight(AddFlightRequest addFlightRequest) {

        Flight flight = new Flight(addFlightRequest);

        if (sameFlightFromTo(flight) || !correctDateFormat(flight)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return flightsRepository.addFlight(flight);
    }

    public void clearFlight() {
        flightsRepository.clearFlight();
    }

    public void deleteFlight(int id) {
        flightsRepository.deleteFlight(id);
    }

    public Flight fetchFlight(int id) {
        return flightsRepository.fetchFlight(id);
    }

    public List<Airport> searchAirports(String search) {
        return flightsRepository.searchAirports(search);
    }

    public PageResult<Flight> searchFlights(SearchFlightRequest searchFlightRequest) {

        if (sameSearchFlightFromTo(searchFlightRequest)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return flightsRepository.searchFlights(searchFlightRequest);
    }
}
