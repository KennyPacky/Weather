package com.example.search.domain;

import com.example.search.pojo.DetailsServerInfoReceiver;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    @ApiModelProperty("Name of the city")
    private String city_name;
    @ApiModelProperty("Weather Information of this city")
    private DetailsServerInfoReceiver<List<DetailsServerInfoReceiver.DetailsServerData.ConsolidatedWeather>>.DetailsServerData data;
}
