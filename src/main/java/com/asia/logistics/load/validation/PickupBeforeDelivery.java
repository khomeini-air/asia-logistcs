package com.asia.logistics.load.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Class-level annotation for validating that the order's pickup date must be before the delivery date.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PickupBeforeDeliveryValidator.class)
@Documented
public @interface PickupBeforeDelivery {
    String message() default "pickup date must be the same or before delivery date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
