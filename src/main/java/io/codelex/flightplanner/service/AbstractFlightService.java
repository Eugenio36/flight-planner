package io.codelex.flightplanner.service;

import io.codelex.flightplanner.modules.Flight;
import io.codelex.flightplanner.modules.SearchFlightRequest;

public abstract class AbstractFlightService {

    protected boolean sameFlightFromTo(Flight flight) {
        return flight.getFrom().equals(flight.getTo());
    }

    protected boolean sameSearchFlightFromTo(SearchFlightRequest searchFlightsRequest) {
        return searchFlightsRequest.getFrom().equals(searchFlightsRequest.getTo());
    }

    protected boolean correctDateFormat(Flight flight) {
        return flight.getDepartureTime().isBefore(flight.getArrivalTime());
    }



}
