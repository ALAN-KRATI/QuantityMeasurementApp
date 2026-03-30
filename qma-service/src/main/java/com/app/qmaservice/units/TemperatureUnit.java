package com.app.qmaservice.units;

import com.app.qmaservice.measurable.IMeasurable;

import java.util.function.Function;

public enum TemperatureUnit implements IMeasurable {

    CELSIUS(
            c -> c,
            c -> c
    ),

    FAHRENHEIT(
            f -> (f - 32) * 5 / 9,
            c -> (c * 9 / 5) + 32
    );

    private final Function<Double, Double> toCelsius;
    private final Function<Double, Double> fromCelsius;

    TemperatureUnit(Function<Double, Double> toCelsius,
                    Function<Double, Double> fromCelsius) {
        this.toCelsius = toCelsius;
        this.fromCelsius = fromCelsius;
    }

    @Override
    public double convertToBase(double value) {
        return toCelsius.apply(value);
    }

    @Override
    public double convertFromBase(double value) {
        return fromCelsius.apply(value);
    }

    @Override
    public double getConversionFactor() {
        return 1.0;
    }

    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public void validOperationSupport(String operation) {
        if ("COMPARE".equals(operation) || "CONVERT".equals(operation)) {
            return;
        }

        throw new UnsupportedOperationException(
                operation + " is not supported for Temperature units"
        );
    }
}