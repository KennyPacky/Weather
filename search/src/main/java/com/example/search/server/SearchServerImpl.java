package com.example.search.server;

import com.example.search.pojo.CityID;
import com.example.search.pojo.DetailsServerData;
import com.example.search.pojo.DetailsServerInfoReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class SearchServerImpl implements SearchServer{
    private final Logger logger = LoggerFactory.getLogger(SearchServerImpl.class);
    private final RestTemplate restTemplate;

    @Autowired
    public SearchServerImpl(@Qualifier("restTemplate") RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public List<DetailsServerData> getCityWeather(List<String> cities) {
        CompletableFuture<CityID>[] futures = new CompletableFuture[cities.size()];
        int idx = 0;
        for(String cityName : cities){
            futures[idx] = CompletableFuture.supplyAsync(() -> getCityIdsByName(cityName));
            idx++;
        }
        Set<Integer> cityIdList = new HashSet<>();
        CompletableFuture.allOf(futures).thenAccept(Void -> {
            for(CompletableFuture<CityID> future : futures){
                cityIdList.addAll(future.join().getData());
            }
        }).join();

        CompletableFuture<DetailsServerData>[] weatherInfo = new CompletableFuture[cityIdList.size()];
        idx = 0;
        for(Integer id : cityIdList){
            weatherInfo[idx] = CompletableFuture.supplyAsync(() -> getCityWeatherById(id).getData());
            idx++;
        }

        List<DetailsServerData> detailsServerData = new ArrayList<>();
        CompletableFuture.allOf(weatherInfo).thenAccept(Void -> {
            for(CompletableFuture<DetailsServerData> future : weatherInfo){
                detailsServerData.add(future.join());
            }
        }).join();
        return detailsServerData;
    }

    private CityID getCityIdsByName(String city_name){
        String url = "http://host.docker.internal:8000/details?city=";
        CityID cityId = restTemplate.getForObject(url + city_name, CityID.class);
        logger.warn("City name {} not found.", city_name);
        return cityId;
    }

    // generate the response from details server
    private DetailsServerInfoReceiver getCityWeatherById(Integer cityId){
        String url = "http://host.docker.internal:8000/details/" + cityId;
        return restTemplate.getForObject(url, DetailsServerInfoReceiver.class);
    }
}
