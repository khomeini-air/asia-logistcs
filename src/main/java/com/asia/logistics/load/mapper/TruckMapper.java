package com.asia.logistics.load.mapper;

import com.asia.logistics.load.dto.TruckDto;
import com.asia.logistics.load.entity.Truck;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TruckMapper {

    /**
     * Map {@link TruckDto} to {@link Truck} entity object
     */
    Truck toEntity(TruckDto truckDto);
}
