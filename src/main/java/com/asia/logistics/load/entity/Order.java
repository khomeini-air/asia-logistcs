package com.asia.logistics.load.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String id;
    private Long payoutCents;
    private Integer weightLbs;
    private Integer volumeCuft;
    private String origin;
    private String destination;
    private LocalDate pickupDate;
    private LocalDate deliveryDate;
    private Boolean hazmat;
}
