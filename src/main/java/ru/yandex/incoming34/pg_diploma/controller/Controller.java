package ru.yandex.incoming34.pg_diploma.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.incoming34.pg_diploma.config.OpenApiConfig;
import ru.yandex.incoming34.pg_diploma.dto.LoadFactorsWithMetaData;
import ru.yandex.incoming34.pg_diploma.dto.NewBookingQuery;
import ru.yandex.incoming34.pg_diploma.dto.PassengerLoadFactorQuery;
import ru.yandex.incoming34.pg_diploma.service.CustomInMemoryCache;
import ru.yandex.incoming34.pg_diploma.service.DataBaseAccessService;

import java.util.Map;

@RestController
public class Controller {

    private final DataBaseAccessService dataBaseAccessService;
    private final CustomInMemoryCache customInMemoryCache;

    public Controller(DataBaseAccessService dataBaseAccessService, CustomInMemoryCache customInMemoryCache) {
        this.dataBaseAccessService = dataBaseAccessService;
        this.customInMemoryCache = customInMemoryCache;
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
    public LoadFactorsWithMetaData passengerLoadFactor(@RequestBody PassengerLoadFactorQuery passengerLoadFactorQuery) {
       return customInMemoryCache.getDataFromCacheOrCallLoadFactorFunction(passengerLoadFactorQuery, "passenger_load_factor");
    }

    @PostMapping("/passenger_load_factor_optimized/")
    public LoadFactorsWithMetaData passengerLoadFactorOptimized(@RequestBody PassengerLoadFactorQuery passengerLoadFactorQuery) {
        return customInMemoryCache.getDataFromCacheOrCallLoadFactorFunction(passengerLoadFactorQuery, "passenger_load_factor_optimized");
    }

    @PostMapping("/new_booking/")
    public String newBooking(@RequestBody NewBookingQuery newBookingQuery) {
         return dataBaseAccessService.newBooking(newBookingQuery);
    }
}
