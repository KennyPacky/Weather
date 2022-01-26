package com.example.search.controller;

import com.example.search.exception.InvalidCityIdException;
import com.example.search.server.SearchServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Search Controller")
@RestController
public class SearchController {
    private final SearchServer searchServer;

    @Autowired
    public SearchController(@Qualifier("searchServerImpl") SearchServer searchServer){
        this.searchServer = searchServer;
    }

    // cities' names need to be split by ','
    @GetMapping("/weather/search")
    public ResponseEntity<?> getDetails(
            @ApiParam(value = "Multiple city names can be spilt by ','")
            @RequestParam(value = "cities") String cities
    ) {
        return new ResponseEntity<>(searchServer.getCityWeather(cities), HttpStatus.OK);
    }

    @ExceptionHandler(InvalidCityIdException.class)
    public ResponseEntity<?> invalidExceptionHandler(){
        return new ResponseEntity<>("Invalid city id is provided.", HttpStatus.BAD_REQUEST);
    }
}
