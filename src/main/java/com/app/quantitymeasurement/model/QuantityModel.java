package com.app.quantitymeasurement.model;

import com.app.quantitymeasurement.quantity.Quantity;
import com.app.quantitymeasurement.units.*;

public class QuantityModel {
    public static Quantity<?> toQuantity(QuantityDTO dto) {
        String unit = dto.getUnit().toUpperCase();

        try {
            try {
                return new Quantity<>(dto.getValue(), LengthUnit.valueOf(unit));
            } catch (Exception ignored) {}

            try {
                return new Quantity<>(dto.getValue(), WeightUnit.valueOf(unit));
            } catch (Exception ignored) {}

            try {
                return new Quantity<>(dto.getValue(), VolumeUnit.valueOf(unit));
            } catch (Exception ignored) {}

            try {
                return new Quantity<>(dto.getValue(), TemperatureUnit.valueOf(unit));
            } catch (Exception ignored) {}

            throw new IllegalArgumentException("Invalid unit: " + unit);

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid unit: " + unit);
        }
    }
}