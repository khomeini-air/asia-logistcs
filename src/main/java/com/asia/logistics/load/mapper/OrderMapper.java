package com.asia.logistics.load.mapper;

import com.asia.logistics.load.dto.OrderDto;
import com.asia.logistics.load.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    /**
     * Map {@link OrderDto} to {@link Order} entity
     */
    Order toEntity(OrderDto orderDto);
}
