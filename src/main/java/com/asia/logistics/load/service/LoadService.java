package com.asia.logistics.load.service;

import com.asia.logistics.load.dto.LoadDto;
import com.asia.logistics.load.dto.OptimizeLoadRequest;

public interface LoadService {
    LoadDto findOptimize(OptimizeLoadRequest request);
}
