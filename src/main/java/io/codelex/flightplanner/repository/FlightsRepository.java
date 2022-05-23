package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.modules.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;


@Repository
public class FlightsRepository {

    private final List<Flight> flightList = new ArrayList<>();
    private int idCount = 1;

    public void clearFlight() {
        flightList.clear();
    }

    public void deleteFlight(int id) {
        flightList.removeIf(flight -> flight.getId() == id);
    }

    public Flight addFlight(Flight flight) {

        flight.setId(idCount);

        if (!sameFlightInList(flight)) {
            idCount++;
            flightList.add(flight);
            return flight;
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    public Flight fetchFlight(int id) {
        return flightList
                .stream()
                .filter((Flight flight) -> flight.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<Airport> searchAirports(String search) {
        String phrase = formatLowerTrim(search);

        return flightList
                .stream()
                .map(Flight::getFrom)
                .filter(airport -> formatLowerTrim(airport.getAirport()).contains(phrase)
                                || formatLowerTrim(airport.getCity()).contains(phrase)
                                || formatLowerTrim(airport.getCountry()).contains(phrase))
                .toList();
    }

    public PageResult<Flight> searchFlights(SearchFlightRequest searchFlightRequest) {
        List<Flight> foundFlights = flightsFound(searchFlightRequest);
        return new PageResult<>(0, foundFlights.size(), foundFlights);
    }

    private List<Flight> flightsFound(SearchFlightRequest searchFlightsRequest) {

        return flightList
                .stream()
                .filter(flight -> flight.getFrom().getAirport().equals(searchFlightsRequest.getFrom())
                                && flight.getTo().getAirport().equals(searchFlightsRequest.getTo())
                                && flight.getDepartureTime().toLocalDate().equals(searchFlightsRequest.getDepartureDate()))
                .collect(Collectors.toList());
    }

    private String formatLowerTrim(String text) {
        return text.toLowerCase().trim();
    }

    private boolean sameFlightInList(Flight flight) {
        return flightList
                .stream()
                .anyMatch(thisFlight -> thisFlight.getFrom().equals(flight.getFrom())
                                && thisFlight.getTo().equals(flight.getTo())
                                && thisFlight.getCarrier().equals(flight.getCarrier())
                                && thisFlight.getDepartureTime().equals(flight.getDepartureTime())
                                && thisFlight.getArrivalTime().equals(flight.getArrivalTime()));
    }
}
