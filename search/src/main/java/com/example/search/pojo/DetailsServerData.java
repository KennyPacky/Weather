package com.example.search.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailsServerData {
    @ApiModelProperty("The uid of the city")
    private Integer woeid;
    @ApiModelProperty("Name of the city")
    private String title;
    private List<ConsolidatedWeather> consolidated_weather = new ArrayList<>();
}