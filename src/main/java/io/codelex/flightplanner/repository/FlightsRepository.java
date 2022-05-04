package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.modules.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


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

    public Flight addFlight(AddFlightRequest flightRequest) {
        Flight flight = new Flight(idCount, flightRequest);

        if (sameFlightFromTo(flight) || !correctDateFormat(flight)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else if (!sameFlightInList(flight)) {
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

        List<Flight> foundList = new ArrayList<>();

        if (sameSearchFlightFromTo(searchFlightsRequest)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        for (Flight thisFlight : flightList) {
            if (searchedFlightsAreEqual(searchFlightsRequest, thisFlight)) {
                foundList.add(thisFlight);
            }
        }
        return foundList;
    }


    private synchronized boolean sameFlightFromTo(Flight flight) {
        return flight.getFrom().equals(flight.getTo());
    }

    private synchronized boolean sameSearchFlightFromTo(SearchFlightRequest request) {
        return request.getFrom().equals(request.getTo());
    }

    private boolean correctDateFormat(Flight flight) {
        return flight.getDepartureTime().isBefore(flight.getArrivalTime());
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

    private boolean searchedFlightsAreEqual(SearchFlightRequest searchFlightRequest, Flight flight) {
        return flight.getFrom().getAirport().equals(searchFlightRequest.getFrom())
                && flight.getTo().getAirport().equals(searchFlightRequest.getTo())
                && flight.getDepartureTime().toLocalDate().equals(searchFlightRequest.getDepartureDate());
    }
}
