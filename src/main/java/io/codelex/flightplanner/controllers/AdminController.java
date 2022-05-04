package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.modules.AddFlightRequest;
import io.codelex.flightplanner.modules.Flight;
import io.codelex.flightplanner.service.FlightsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/admin-api/flights")
public class AdminController {

    private final FlightsService flightsService;

    public AdminController(FlightsService flightsService) {
        this.flightsService = flightsService;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public synchronized Flight addFlight(@Valid @RequestBody AddFlightRequest flightRequest) {
        return flightsService.addFlight(flightRequest);
    }

    @DeleteMapping ("/{id}")
    public synchronized void deleteFlight(@PathVariable int id) {
        flightsService.deleteFlight(id);
    }

    @GetMapping ("/{id}")
    public Flight fetchFlight(@PathVariable int id) {
        return flightsService.fetchFlight(id);
    }

}
