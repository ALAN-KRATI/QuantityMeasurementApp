package com.app.qmaservice.service;

import com.app.qmaservice.dto.QuantityInputDTO;
import com.app.qmaservice.entity.QuantityMeasurementEntity;

import java.util.List;

public interface QuantityService {

    QuantityMeasurementEntity compare(QuantityInputDTO input, String userEmail);

    QuantityMeasurementEntity convert(QuantityInputDTO input, String userEmail);

    QuantityMeasurementEntity add(QuantityInputDTO input, String userEmail);

    QuantityMeasurementEntity subtract(QuantityInputDTO input, String userEmail);

    QuantityMeasurementEntity divide(QuantityInputDTO input, String userEmail);

    List<QuantityMeasurementEntity> getHistory(String userEmail);
}