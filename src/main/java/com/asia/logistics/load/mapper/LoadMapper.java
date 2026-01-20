package com.asia.logistics.load.mapper;

import com.asia.logistics.load.dto.LoadDto;
import com.asia.logistics.load.entity.Load;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoadMapper {
    /**
     * Map entity {@link Load} to {@link LoadDto}
     */
    LoadDto toDto(Load load);
}
