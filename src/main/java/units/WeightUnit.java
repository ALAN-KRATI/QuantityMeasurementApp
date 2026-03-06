package units;

import measurable.IMeasurable;
import support.SupportsArithmetic;

public enum WeightUnit implements IMeasurable {

    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double toKGFactor;

    private static final SupportsArithmetic sm = () -> true;

    WeightUnit(double toKGFactor) {
        this.toKGFactor = toKGFactor;
    }

    @Override
    public double convertToBase(double value) {
        return value * toKGFactor;
    }

    @Override
    public double convertFromBase(double value) {
        return value / toKGFactor;
    }

    @Override
    public double getConversionFactor() {
        return toKGFactor;
    }

    @Override
    public String getUnitName() {
        return this.name();
    }
}