package com.asia.logistics.load.dto;

import com.asia.logistics.load.validation.PickupBeforeDelivery;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
@PickupBeforeDelivery
public class OrderDto {
    @NotBlank(message = "order id is missing")
    private String id;

    @JsonProperty("payout_cents")
    @NotNull(message = "order payoutCents value is missing")
    @Positive(message = "order payoutCents value is not positive")
    private Long payoutCents;

    @JsonProperty("weight_lbs")
    @NotNull(message = "order weight value is missing")
    @Positive(message = "order weight value is not positive")
    private Integer weightLbs;

    @JsonProperty("volume_cuft")
    @NotNull(message = "order volume value is missing")
    @Positive(message = "order volume value is not positive")
    private Integer volumeCuft;

    @JsonProperty("origin")
    @NotBlank(message = "order origin value is missing")
    private String origin;

    @JsonProperty("destination")
    @NotBlank(message = "order destination value is missing")
    private String destination;

    @JsonProperty("pickup_date")
    @NotNull(message = "order pickup date is missing")
    @Future(message = "order pickup date must be in the future")
    private LocalDate pickupDate;

    @JsonProperty("delivery_date")
    @NotNull(message = "order delivery date is missing")
    @Future(message = "order delivery date must be in the future")
    private LocalDate deliveryDate;

    @JsonProperty("is_hazmat")
    private Boolean hazmat;
}
