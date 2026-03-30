package com.app.qmaservice.controller;

import com.app.qmaservice.dto.HistoryResponse;
import com.app.qmaservice.dto.QuantityInputDTO;
import com.app.qmaservice.entity.QuantityMeasurementEntity;
import com.app.qmaservice.service.HistoryService;
import com.app.qmaservice.service.QuantityService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quantities")
public class QuantityController {

    private final QuantityService quantityService;
    private final HistoryService historyService;

    public QuantityController(QuantityService quantityService,
                              HistoryService historyService) {
        this.quantityService = quantityService;
        this.historyService = historyService;
    }

    @PostMapping("/add")
    public QuantityMeasurementEntity add(@RequestBody QuantityInputDTO input,
                                         Authentication authentication) {
        return quantityService.add(input, authentication.getName());
    }

    @PostMapping("/subtract")
    public QuantityMeasurementEntity subtract(@RequestBody QuantityInputDTO input,
                                              Authentication authentication) {
        return quantityService.subtract(input, authentication.getName());
    }

    @PostMapping("/divide")
    public QuantityMeasurementEntity divide(@RequestBody QuantityInputDTO input,
                                            Authentication authentication) {
        return quantityService.divide(input, authentication.getName());
    }

    @PostMapping("/compare")
    public QuantityMeasurementEntity compare(@RequestBody QuantityInputDTO input,
                                             Authentication authentication) {
        return quantityService.compare(input, authentication.getName());
    }

    @PostMapping("/convert")
    public QuantityMeasurementEntity convert(@RequestBody QuantityInputDTO input,
                                             Authentication authentication) {
        return quantityService.convert(input, authentication.getName());
    }

    @GetMapping("/history")
    public List<HistoryResponse> history(Authentication authentication) {
        return historyService.getHistory(authentication.getName());
    }
}