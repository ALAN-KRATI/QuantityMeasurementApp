package com.app.qmaservice.util;

import com.app.qmaservice.dto.QuantityDTO;
import com.app.qmaservice.quantity.Quantity;
import com.app.qmaservice.units.LengthUnit;
import com.app.qmaservice.units.TemperatureUnit;
import com.app.qmaservice.units.VolumeUnit;
import com.app.qmaservice.units.WeightUnit;

public class QuantityModel {

    public static Quantity<?> toQuantity(QuantityDTO dto) {
        String unit = dto.getUnit().toUpperCase();

        try {
            return new Quantity<LengthUnit>(
                    dto.getValue(),
                    LengthUnit.valueOf(unit)
            );
        } catch (Exception ignored) {
        }

        try {
            return new Quantity<WeightUnit>(
                    dto.getValue(),
                    WeightUnit.valueOf(unit)
            );
        } catch (Exception ignored) {
        }

        try {
            return new Quantity<VolumeUnit>(
                    dto.getValue(),
                    VolumeUnit.valueOf(unit)
            );
        } catch (Exception ignored) {
        }

        try {
            return new Quantity<TemperatureUnit>(
                    dto.getValue(),
                    TemperatureUnit.valueOf(unit)
            );
        } catch (Exception ignored) {
        }

        throw new IllegalArgumentException("Invalid unit: " + unit);
    }
}