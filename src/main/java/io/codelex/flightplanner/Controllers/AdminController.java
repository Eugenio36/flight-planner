package io.codelex.flightplanner.Controllers;

import io.codelex.flightplanner.Modules.AddFlightRequest;
import io.codelex.flightplanner.Modules.Flight;
import io.codelex.flightplanner.Service.FlightsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/admin-api")
public class AdminController {

    private final FlightsService flightsService;

    public AdminController(FlightsService flightsService) {
        this.flightsService = flightsService;
    }

    @PutMapping("/flights")
    @ResponseStatus(HttpStatus.CREATED)
    public synchronized Flight addFlight(@Valid @RequestBody AddFlightRequest flightRequest) {
        return flightsService.addFlight(flightRequest);
    }

    @DeleteMapping ("/flights/{id}")
    public synchronized void deleteFlight(@PathVariable int id) {
        flightsService.deleteFlight(id);
    }

    @GetMapping ("/flights/{id}")
    public synchronized Flight fetchFlight(@PathVariable int id) {
        return flightsService.fetchFlight(id);
    }

}
