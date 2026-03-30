package com.app.qmaservice.units;

import com.app.qmaservice.measurable.IMeasurable;

public enum VolumeUnit implements IMeasurable {

    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private final double conversionFactor;

    VolumeUnit(double conversionFactor) {
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
                        operation + " is not supported for Volume units"
                );
        }
    }
}