package com.example.search.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsolidatedWeather{
    private String weather_state_name;
    private String applicable_date;
    private double min_temp;
    private double max_temp;
    private Integer predictability;
}