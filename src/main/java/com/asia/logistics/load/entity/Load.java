package com.asia.logistics.load.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Load {
    private String id;
    private String truckId;
    private List<String> orderIds;
    private Long totalPayoutCents;
    private Integer totalWeightLbs;
    private Integer totalVolumeCuft;
    private BigDecimal weightPercentage;
    private BigDecimal volumePercentage;
}
