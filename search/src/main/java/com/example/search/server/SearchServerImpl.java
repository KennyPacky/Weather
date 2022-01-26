package com.example.search.server;

import com.example.search.domain.SearchResponse;
import com.example.search.pojo.CityID;
import com.example.search.pojo.DetailsServerInfoReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public List<SearchResponse> getCityWeather(String city_name) {
        List<SearchResponse> searchResponseList = new ArrayList<>();
        String[] cityNameList = city_name.split(",");
        // avoid duplicate city
        Map<Integer, CompletableFuture<SearchResponse>> futureList = new HashMap<>();
        for (String cityName : cityNameList) {
            CityID cityId = restTemplate.getForObject("http://host.docker.internal:8000/details?city=" + cityName, CityID.class);
            if (cityId == null) {
                logger.error("city_name, {} - fail to query data from Details Server", cityName);
                continue;
            }
            if (cityId.getData().size() == 0) {
                logger.warn("city_name, {} not found", cityName);
                continue;
            }
            List<Integer> list = cityId.getData();
            // multiple city may found for a same name
            for (int id : list) {
                if (futureList.get(id) == null) {
                    futureList.put(id, CompletableFuture.supplyAsync(() -> getWeatherDetails(id))
                            .thenApply(response -> new SearchResponse(response.getData().getTitle(), response.getData())));
                }
            }
        }

        // convert into array for allOf()
        CompletableFuture<SearchResponse>[] futures = new CompletableFuture[futureList.size()];
        int idx = 0;
        for(CompletableFuture<SearchResponse> response : futureList.values()){
            futures[idx] = response;
            idx++;
        }
        CompletableFuture.allOf(futures).thenAccept(Void -> {
            for (CompletableFuture<SearchResponse> future : futures) {
                searchResponseList.add(future.join());
            }
        }).join();
        return searchResponseList;
    }

    // generate the response from details server
    private DetailsServerInfoReceiver<List<DetailsServerInfoReceiver.DetailsServerData.ConsolidatedWeather>> getWeatherDetails(Integer cityId){
        String url = "http://host.docker.internal:8000/details/" + cityId;
        ParameterizedTypeReference<DetailsServerInfoReceiver<List<DetailsServerInfoReceiver.DetailsServerData.ConsolidatedWeather>>> typeReference
                = new ParameterizedTypeReference<DetailsServerInfoReceiver<List<DetailsServerInfoReceiver.DetailsServerData.ConsolidatedWeather>>>() {};
        return restTemplate.exchange(url, HttpMethod.GET, null, typeReference).getBody();
    }
}
