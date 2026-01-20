package com.asia.logistics.load.service;

import com.asia.logistics.load.entity.Load;
import com.asia.logistics.load.entity.Order;
import com.asia.logistics.load.entity.Truck;

import java.util.List;

public interface LoadOptimizer {
    Load optimize(Truck truck, List<Order> orders);
}
