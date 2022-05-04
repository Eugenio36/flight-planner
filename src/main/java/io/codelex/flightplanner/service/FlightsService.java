package io.codelex.flightplanner.service;

import io.codelex.flightplanner.modules.*;
import io.codelex.flightplanner.repository.FlightsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightsService {

    private final FlightsRepository flightsRepository;

    public FlightsService(FlightsRepository flightsRepository) {
        this.flightsRepository = flightsRepository;
    }

    public Flight addFlight(AddFlightRequest addFlightRequest) {
        return flightsRepository.addFlight(addFlightRequest);
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
        return flightsRepository.searchFlights(searchFlightRequest);
    }
}
