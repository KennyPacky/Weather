package com.example.search.server;

import com.example.search.domain.SearchResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SearchServer {
    List<SearchResponse> getCityWeather(String city_name);
}
