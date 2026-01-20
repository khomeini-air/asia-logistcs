package com.asia.logistics.load.service;

import com.asia.logistics.load.entity.LoadPartitionKey;
import com.asia.logistics.load.entity.Order;

import java.util.List;
import java.util.Map;

public interface OrderPartitionService {
    /**
     * Partitions orders into independent optimization groups
     * based on:
     * 1) Hazmat compatibility
     * 2) Origin + destination (route)
     */
    Map<LoadPartitionKey, List<Order>> partitionByHazmatAndRoute(List<Order> orders);
}
