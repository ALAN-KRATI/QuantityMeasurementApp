package com.app.qmaservice.units;

import com.app.qmaservice.measurable.IMeasurable;

public enum LengthUnit implements IMeasurable {

    FEET(1.0),
    INCH(1.0 / 12),
    YARDS(3.0),
    CENTIMETER(0.03280839895);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override
    public double convertToBase(double value) {
        return value * conversionFactor;
    }

    @Override
    public double convertFromBase(double value) {
        return value / conversionFactor;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public void validOperationSupport(String operation) {
        switch (operation) {
            case "ADD":
            case "SUBTRACT":
            case "DIVIDE":
            case "COMPARE":
            case "CONVERT":
                return;

            default:
                throw new UnsupportedOperationException(
                        operation + " is not supported for Length units"
                );
        }
    }
}