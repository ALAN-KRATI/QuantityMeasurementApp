package com.app.quantitymeasurement.model;

public class QuantityDTO {

    private final double value;
    private final String unit;

    public QuantityDTO(double value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }
}