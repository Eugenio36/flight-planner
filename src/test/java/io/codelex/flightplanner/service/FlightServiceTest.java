package io.codelex.flightplanner.service;

import io.codelex.flightplanner.modules.AddFlightRequest;
import io.codelex.flightplanner.modules.Airport;
import io.codelex.flightplanner.modules.Flight;
import io.codelex.flightplanner.repository.FlightsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {

    @Mock
    FlightsRepository flightsRepository;

    @InjectMocks
    FlightsService flightsService;

    @Test
    public void testAddFlight() {

        Airport from = new Airport("Latvia", "Riga", "RIX");
        Airport to = new Airport("Estonia", "Tallin", "EST");
        String carrier = "Carrier";
        String departureTime = "2022-06-01 17:35";
        String arrivalTime = "2022-06-02 11:35";

        AddFlightRequest addFlightRequest = new AddFlightRequest(from, to, carrier, departureTime, arrivalTime);

        Mockito.doAnswer(invocation -> {
            AddFlightRequest request = invocation.getArgument(0);
            Assertions.assertEquals(addFlightRequest, request);
            return new Flight(22, request);
        }).when(flightsRepository).addFlight(addFlightRequest);


        Flight flight = flightsRepository.addFlight(addFlightRequest);

        Mockito.verify(flightsRepository).addFlight(addFlightRequest);
    }

}
