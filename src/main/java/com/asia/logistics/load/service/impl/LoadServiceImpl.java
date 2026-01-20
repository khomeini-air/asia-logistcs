package com.asia.logistics.load.service.impl;

import com.asia.logistics.load.dto.LoadDto;
import com.asia.logistics.load.dto.OptimizeLoadRequest;
import com.asia.logistics.load.entity.Load;
import com.asia.logistics.load.entity.LoadPartitionKey;
import com.asia.logistics.load.entity.Order;
import com.asia.logistics.load.entity.Truck;
import com.asia.logistics.load.exception.IllegalLoadOrdersException;
import com.asia.logistics.load.mapper.LoadMapper;
import com.asia.logistics.load.mapper.OrderMapper;
import com.asia.logistics.load.mapper.TruckMapper;
import com.asia.logistics.load.service.LoadOptimizer;
import com.asia.logistics.load.service.LoadService;
import com.asia.logistics.load.service.OrderPartitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoadServiceImpl implements LoadService {

    private final LoadOptimizer loadOptimizer;
    private final OrderPartitionService partitionService;
    private final LoadMapper loadMapper;
    private final OrderMapper orderMapper;
    private final TruckMapper truckMapper;

    @Override
    @Cacheable(value = "load-optimizer", keyGenerator = "optimizeLoadKeyGenerator")
    public LoadDto findOptimize(OptimizeLoadRequest request) {
        String truckId = request.getTruck().getId();
        Truck truck = truckMapper.toEntity(request.getTruck());
        List<Order> orders = request
                .getOrders()
                .stream()
                .map(orderMapper::toEntity)
                .toList();

        // Partition orders by hazmat and route
        Map<LoadPartitionKey, List<Order>> orderGroups = partitionOrder(orders);

        log.info("Evaluating {} independent order groups for truckId={}", orderGroups.size(), truckId);

        // Optimize each group independently and get the best Load.
        Load bestLoad = findBestLoad(orderGroups, truck);

        log.info(
                "Optimization finished for truckId={}, selectedOrders={}, totalPayout={}",
                truckId,
                bestLoad.getOrderIds().size(),
                bestLoad.getTotalPayoutCents()
        );

        return loadMapper.toDto(bestLoad);
    }

    private Map<LoadPartitionKey, List<Order>> partitionOrder(List<Order> orders) {
        Map<LoadPartitionKey, List<Order>> orderGroups = partitionService.partitionByHazmatAndRoute(orders);

        if (orderGroups.isEmpty()) {
            log.error("No compatible order groups found");
            throw new IllegalLoadOrdersException("No compatible order group");
        }

        return orderGroups;
    }

    private Load findBestLoad(Map<LoadPartitionKey, List<Order>> orderGroups, Truck truck) {
        return orderGroups
                .values()
                .stream()
                .map(group -> loadOptimizer.optimize(truck, group))
                .max(Comparator.comparingLong(Load::getTotalPayoutCents))
                .orElseThrow(() -> {
                    // This should not be executed since empty order list is not allowed from the controller.
                    // Hence, we log it as an error to alert for further investigation.
                    String message = String.format("Load optimizer - empty order detected. truckId=%s", truck.getId());
                    log.error(message);
                    return new IllegalLoadOrdersException(message);
                });
    }
}
