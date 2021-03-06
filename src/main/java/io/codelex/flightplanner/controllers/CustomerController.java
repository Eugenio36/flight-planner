package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.modules.Airport;
import io.codelex.flightplanner.modules.Flight;
import io.codelex.flightplanner.modules.PageResult;
import io.codelex.flightplanner.modules.SearchFlightRequest;
import io.codelex.flightplanner.service.FlightsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {

    private final FlightsService flightsService;

    public CustomerController(FlightsService flightsService) {
        this.flightsService = flightsService;
    }

    @GetMapping("/airports")
    public List<Airport> searchAirports(@PathParam("search") String search) {
        return flightsService.searchAirports(search);
    }

    @PostMapping("/flights/search")
    public PageResult<Flight> searchFlights(@Valid @RequestBody SearchFlightRequest searchFlightRequest) {
        return flightsService.searchFlights(searchFlightRequest);
    }

    @GetMapping ("/flights/{id}")
    public Flight findFlightById(@PathVariable int id) {
        return flightsService.fetchFlight(id);
    }
}
