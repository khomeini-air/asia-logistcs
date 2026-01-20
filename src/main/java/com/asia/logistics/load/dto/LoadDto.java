package com.asia.logistics.load.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class LoadDto {
    @JsonProperty("truck_id")
    private String truckId;

    @JsonProperty("selected_order_ids")
    private List<String> orderIds;

    @JsonProperty("total_payout_cents")
    private Long totalPayoutCents;

    @JsonProperty("total_weight_lbs")
    private Integer totalWeightLbs;

    @JsonProperty("total_volume_cuft")
    private Integer totalVolumeCuft;

    @JsonProperty("utilization_weight_percent")
    private BigDecimal weightPercentage;

    @JsonProperty("utilization_volume_percent")
    private BigDecimal volumePercentage;
}
