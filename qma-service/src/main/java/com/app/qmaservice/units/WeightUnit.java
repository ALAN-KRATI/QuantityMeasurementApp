package com.app.qmaservice.units;

import com.app.qmaservice.measurable.IMeasurable;

public enum WeightUnit implements IMeasurable {

    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
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
                        operation + " is not supported for Weight units"
                );
        }
    }
}