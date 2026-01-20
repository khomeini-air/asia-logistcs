package com.asia.logistics.load.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class TruckDto {
    @NotBlank(message = "Truck id is missing")
    private String id;

    @JsonProperty("max_weight_lbs")
    @NotNull(message = "Truck max weight is missing")
    @Positive(message = "Truck max weight must be a positive number")
    private Integer maxWeightLbs;

    @JsonProperty("max_volume_cuft")
    @NotNull(message = "Truck max volume is missing")
    @Positive(message = "Truck max volume must be positive number")
    private Integer maxVolumeCuft;
}
