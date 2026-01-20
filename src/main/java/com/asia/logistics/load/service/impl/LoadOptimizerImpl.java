package com.asia.logistics.load.service.impl;

import com.asia.logistics.load.entity.Load;
import com.asia.logistics.load.entity.Order;
import com.asia.logistics.load.entity.Truck;
import com.asia.logistics.load.service.LoadOptimizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class LoadOptimizerImpl implements LoadOptimizer {
    /**
     * Optimizes the load for the given truck and orders.
     * Uses bitmask dynamic programming (n <= 22).
     */
    public Load optimize(Truck truck, List<Order> orders) {
        log.debug("Finding the optimal load for truckId={}, orders={}", truck.getId(), orders.size());

        int bestMask = calculateBestMask(truck, orders);
        return buildLoad(truck, orders, bestMask);
    }

    /**
     * Iterates through all possible subsets of orders and
     * returns the bitmask that yields the highest payout
     * without exceeding truck capacity.
     */
    private int calculateBestMask(Truck truck, List<Order> orders) {

        int n = orders.size();
        int bestMask = 0;
        int bestPayout = 0;

        // Iterate through all subsets (2^n)
        for (int mask = 1; mask < (1 << n); mask++) {

            int totalWeight = 0;
            int totalVolume = 0;
            int totalPayout = 0;

            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) == 0) continue;

                Order order = orders.get(i);
                totalWeight += order.getWeightLbs();
                totalVolume += order.getVolumeCuft();

                // Pruning when capacity is exceeded
                if (exceedsCapacity(truck, totalWeight, totalVolume)) {
                    totalPayout = -1;
                    break;
                }

                totalPayout += order.getPayoutCents();
            }

            if (totalPayout > bestPayout) {
                bestPayout = totalPayout;
                bestMask = mask;
            }
        }

        log.debug("Best mask calculated for truckId={}, bestPayout={}", truck.getId(), bestPayout);

        return bestMask;
    }

    /**
     * Builds a Load aggregate from the selected bitmask.
     */
    private Load buildLoad(Truck truck, List<Order> orders, int mask) {

        List<String> orderIds = new ArrayList<>();
        int totalWeight = 0;
        int totalVolume = 0;
        long totalPayout = 0;

        for (int i = 0; i < orders.size(); i++) {
            if ((mask & (1 << i)) != 0) {
                Order o = orders.get(i);
                orderIds.add(o.getId());
                totalWeight += o.getWeightLbs();
                totalVolume += o.getVolumeCuft();
                totalPayout += o.getPayoutCents();
            }
        }

        return Load.builder()
                .id(UUID.randomUUID().toString())
                .truckId(truck.getId())
                .orderIds(orderIds)
                .totalPayoutCents(totalPayout)
                .totalWeightLbs(totalWeight)
                .totalVolumeCuft(totalVolume)
                .weightPercentage(percentage(totalWeight, truck.getMaxWeightLbs()))
                .volumePercentage(percentage(totalVolume, truck.getMaxVolumeCuft()))
                .build();
    }

    /**
     * Checks if capacity limits are exceeded.
     */
    private boolean exceedsCapacity(Truck truck, int weight, int volume) {
        return weight > truck.getMaxWeightLbs()
                || volume > truck.getMaxVolumeCuft();
    }

    /**
     * Calculates utilization percentage with 2-decimal precision.
     */
    private BigDecimal percentage(int used, int max) {
        if (max == 0) return BigDecimal.ZERO;

        return BigDecimal.valueOf(used)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(max), 2, RoundingMode.HALF_UP);
    }
}
