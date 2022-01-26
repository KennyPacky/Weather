package com.example.search.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Detail Server Entity")
public class DetailsServerInfoReceiver<T> {
    private Integer code;
    private String timestamp;
    private DetailsServerData data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class DetailsServerData {
        @ApiModelProperty("The uid of the city")
        private Integer woeid;
        @ApiModelProperty("Name of the city")
        private String title;
        private T consolidated_weather;

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
    }
}