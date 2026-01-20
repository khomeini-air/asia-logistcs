package com.asia.logistics.load.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Truck {
    private String id;
    private Integer maxWeightLbs;
    private Integer maxVolumeCuft;
}
