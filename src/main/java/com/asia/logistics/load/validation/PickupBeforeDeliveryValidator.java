package com.asia.logistics.load.validation;

import com.asia.logistics.load.dto.OrderDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class PickupBeforeDeliveryValidator implements ConstraintValidator<PickupBeforeDelivery, OrderDto> {
    @Override
    public boolean isValid(OrderDto order, ConstraintValidatorContext context) {
        // Handle null cases with @NotNull
        if (order == null) {
            return true;
        }

        LocalDate pickupDate = order.getPickupDate();
        LocalDate deliveryDate = order.getDeliveryDate();

        // Handle null cases with @NotNull or @Future
        if (pickupDate == null || deliveryDate == null) {
            return true;
        }

        return !pickupDate.isAfter(deliveryDate);
    }
}
