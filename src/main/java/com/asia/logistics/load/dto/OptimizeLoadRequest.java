package com.asia.logistics.load.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptimizeLoadRequest {
    @NotNull(message = "truck parameter is missing")
    @Valid
    private TruckDto truck;

    @NotEmpty(message = "orders parameter is missing ")
    @Valid
    private List<OrderDto> orders;
}
