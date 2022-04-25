package io.codelex.flightplanner.Repository;

import io.codelex.flightplanner.Modules.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        throw new ResponseStatusException(HttpStatus.OK);
    }

    public Flight addFlight(AddFlightRequest flightRequest) {

        Flight flight = new Flight(idCount, flightRequest);

        if (flight.getFrom().equals(flight.getTo()) || !correctDateFormat(flight)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        } else if (!sameFlightInList(flight)) {
            idCount++;
            flightList.add(flight);
            return flight;

        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    private boolean correctDateFormat(Flight flight) {
        return flight.getDepartureTime().isBefore(flight.getArrivalTime());
    }

    private boolean sameFlightInList(Flight flight) {
        boolean sameFlight = false;
        for (Flight thisFlight: flightList) {
            if (flight.getFrom().equals(thisFlight.getFrom())
                    && flight.getTo().equals(thisFlight.getTo())
                    && flight.getCarrier().equals(thisFlight.getCarrier())
                    && flight.getDepartureTime().equals(thisFlight.getDepartureTime())
                    && flight.getArrivalTime().equals(thisFlight.getArrivalTime())) {
                sameFlight = true;
                break;
            }
        }
        return sameFlight;
    }

    public Flight fetchFlight(int id) {
        Flight found = null;
        for (Flight thisFlight: flightList) {
            if (thisFlight.getId() == id) {
                found = thisFlight;
            }
        }
        if (found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return found;
        }
    }

    public List<Airport> searchAirports(String search) {

        final List<Airport> airportList = new ArrayList<>();
        String phrase = search.toLowerCase().trim();

        for (Flight thisFlight : flightList) {

            if (thisFlight.getFrom().getCountry().toLowerCase().trim().contains(phrase)
                    || thisFlight.getFrom().getCity().toLowerCase().trim().contains(phrase)
                    || thisFlight.getFrom().getAirport().toLowerCase().trim().contains(phrase)) {
                airportList.add(thisFlight.getFrom());
            }
        }
        return airportList;
    }


    public PageResult<Flight> searchFlights(SearchFlightRequest searchFlightRequest) {

        List<Flight> foundFlights = flightsFound(searchFlightRequest);

        return new PageResult<>(0, foundFlights.size(), foundFlights);
    }

    private List<Flight> flightsFound(SearchFlightRequest searchFlightsRequest) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate searchFlightDepartureTime = LocalDate.parse(searchFlightsRequest.getDepartureDate(), formatter);
        List<Flight> foundList = new ArrayList<>();

        if (searchFlightsRequest.getFrom().equals(searchFlightsRequest.getTo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        for (Flight thisFlight : flightList) {

            if (thisFlight.getFrom().getAirport().equals(searchFlightsRequest.getFrom())
                    && thisFlight.getTo().getAirport().equals(searchFlightsRequest.getTo())
                    && thisFlight.getDepartureTime().toLocalDate().equals(searchFlightDepartureTime)) {
                foundList.add(thisFlight);
            }
        }
        return foundList;
    }


}
