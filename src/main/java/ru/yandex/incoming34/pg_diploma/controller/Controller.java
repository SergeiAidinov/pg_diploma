package ru.yandex.incoming34.pg_diploma.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.incoming34.pg_diploma.config.OpenApiConfig;
import ru.yandex.incoming34.pg_diploma.service.DataBaseAccessService;

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

    @GetMapping("/func/{aircraftId}")
    public Integer callFunction(@RequestParam String aircraftId){
        return dataBaseAccessService.callFunction(aircraftId);
    }

    @GetMapping("/quantity_of_bookings_by_quantity_of_passengers_in_one_booking/")
    public Map<Integer, Integer> quantityOfBookingsByQuantityOfPassengersInOneBooking() {
        return dataBaseAccessService.quantityOfBookingsByQuantityOfPassengersInOneBooking();
    }
}
