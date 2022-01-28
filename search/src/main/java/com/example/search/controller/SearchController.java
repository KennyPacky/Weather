package com.example.search.controller;

import com.example.search.exception.InvalidCityIdException;
import com.example.search.pojo.DetailsServerData;
import com.example.search.server.SearchServer;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Search Controller")
@RestController
public class SearchController {
    private final SearchServer searchServer;
    private final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    public SearchController(@Qualifier("searchServerImpl") SearchServer searchServer){
        this.searchServer = searchServer;
    }

    @GetMapping("/weather/search")
    public ResponseEntity<List<DetailsServerData>> getDetails(
            @RequestBody List<String> cities
    ) {
        return new ResponseEntity<>(searchServer.getCityWeather(cities), HttpStatus.OK);
    }

    @ExceptionHandler(InvalidCityIdException.class)
    public ResponseEntity<?> invalidExceptionHandler(){
        return new ResponseEntity<>("Invalid city id is provided.", HttpStatus.BAD_REQUEST);
    }
}
