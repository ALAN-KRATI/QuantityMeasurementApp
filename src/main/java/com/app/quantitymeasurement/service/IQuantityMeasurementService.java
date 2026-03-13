package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.quantity.Quantity;

public interface IQuantityMeasurementService {

    QuantityMeasurementEntity compare(Quantity<?> q1, Quantity<?> q2);

    QuantityMeasurementEntity convert(Quantity<?> quantity, Object targetUnit);

    QuantityMeasurementEntity add(Quantity<?> q1, Quantity<?> q2);

    QuantityMeasurementEntity subtract(Quantity<?> q1, Quantity<?> q2);

    QuantityMeasurementEntity divide(Quantity<?> q1, Quantity<?> q2);

}