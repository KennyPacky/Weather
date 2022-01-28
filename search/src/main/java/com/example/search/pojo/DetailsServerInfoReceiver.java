package com.example.search.pojo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Detail Server Entity")
public class DetailsServerInfoReceiver {
    private Integer code;
    private String timestamp;
    private DetailsServerData data;
}