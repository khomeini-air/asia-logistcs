package com.asia.logistics.load.api;

import com.asia.logistics.load.dto.LoadDto;
import com.asia.logistics.load.dto.OptimizeLoadRequest;
import com.asia.logistics.load.service.LoadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/load-optimizer")
@RequiredArgsConstructor
@Validated
public class OptimizeLoadController {

    private final LoadService loadService;

    @PostMapping("/optimize")
    public ResponseEntity<LoadDto> optimize(@Valid @RequestBody OptimizeLoadRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(loadService.findOptimize(request));
    }
}
