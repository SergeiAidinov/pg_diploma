package ru.yandex.incoming34.pg_diploma.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.incoming34.pg_diploma.config.OpenApiConfig;
import ru.yandex.incoming34.pg_diploma.dto.PassengerLoadFactor;
import ru.yandex.incoming34.pg_diploma.dto.PassengerLoadFactorQuery;
import ru.yandex.incoming34.pg_diploma.service.DataBaseAccessService;

import java.util.List;
import java.util.Map;

@RestController
public class Controller {

    private final DataBaseAccessService dataBaseAccessService;

    public Controller(DataBaseAccessService dataBaseAccessService) {
        this.dataBaseAccessService = dataBaseAccessService;
    }

    @GetMapping("/about")
    public String hello(){
        return OpenApiConfig.TITLE;
    }

    @GetMapping("/distance/{aircraftId}")
    public Integer getRangeByAircraftId(@RequestParam String aircraftId){
        return dataBaseAccessService.getRangeByAircraftId(aircraftId);
    }

    @GetMapping("/quantity_of_bookings_by_quantity_of_passengers_in_one_booking/")
    public Map<Integer, Integer> quantityOfBookingsByQuantityOfPassengersInOneBooking() {
        return dataBaseAccessService.quantityOfBookingsByQuantityOfPassengersInOneBooking();
    }

    @PostMapping("/passenger_load_factor/")
    public List<PassengerLoadFactor> passengerLoadFactor(@RequestBody PassengerLoadFactorQuery passengerLoadFactorQuery) {
       return dataBaseAccessService.passengerLoadFactor(passengerLoadFactorQuery);
    }
}
