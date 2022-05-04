package io.codelex.flightplanner.controllers;


import io.codelex.flightplanner.service.FlightsService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/testing-api")
public class TestController {

    private final FlightsService flightsService;

    public TestController(FlightsService flightsService) {
        this.flightsService = flightsService;
    }

    @PostMapping("/clear")
    public void clearFlight() {
        flightsService.clearFlight();
    }
}

