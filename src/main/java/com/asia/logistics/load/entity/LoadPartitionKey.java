package com.asia.logistics.load.entity;

public record LoadPartitionKey(String origin, String destination, Boolean isHazmat) {}
