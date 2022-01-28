package com.example.search.server;

import com.example.search.pojo.DetailsServerData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SearchServer {
    List<DetailsServerData> getCityWeather(List<String> city_name);
}
