package com.app.quantitymeasurement.units;
import com.app.quantitymeasurement.measurable.IMeasurable;
import com.app.quantitymeasurement.support.SupportsArithmetic;

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

    private static final SupportsArithmetic supportsArithmetic = () -> false;

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
    public double convertFromBase(double baseValue) {
        return fromCelsius.apply(baseValue);
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
        if (!supportsArithmetic.isSupported()) {
            throw new UnsupportedOperationException(
                    "Arithmetic operation not supported for Temperature"
            );
        }
    }
}