package com.asia.logistics.load.service.impl;

import com.asia.logistics.load.entity.LoadPartitionKey;
import com.asia.logistics.load.entity.Order;
import com.asia.logistics.load.service.OrderPartitionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderPartitionServiceImpl implements OrderPartitionService {
    public Map<LoadPartitionKey, List<Order>> partitionByHazmatAndRoute(List<Order> orders) {
        Map<LoadPartitionKey, List<Order>> result = new HashMap<>();

        // Group the order based on hazmat vs non-hazmat
        Map<Boolean, List<Order>> byHazmat = orders.stream().collect(Collectors.partitioningBy(Order::getHazmat));

        byHazmat.forEach((isHazmat, hazmatOrders) -> {
            if (hazmatOrders.isEmpty()) {
                return;
            }

            // Group by route
            Map<LoadPartitionKey, List<Order>> byRoute = hazmatOrders
                    .stream()
                    .collect(Collectors.groupingBy(
                            o -> new LoadPartitionKey(o.getOrigin(), o.getDestination(), o.getHazmat()))
                    );

            log.debug(
                    "Partitioned {} orders into {} route groups (hazmat={})",
                    hazmatOrders.size(),
                    byRoute.size(),
                    isHazmat
            );

            result.putAll(byRoute);
        });

        return result;
    }
}
