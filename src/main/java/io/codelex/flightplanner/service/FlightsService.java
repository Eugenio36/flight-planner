package io.codelex.flightplanner.service;

import io.codelex.flightplanner.modules.*;

import java.util.List;

public interface FlightsService {

    void clearFlight();

    void deleteFlight(int id);

    Flight addFlight(AddFlightRequest addFlightRequest);

    Flight fetchFlight(int id);

    List<Airport> searchAirports(String search);

    PageResult<Flight> searchFlights(SearchFlightRequest searchFlightRequest);
}
