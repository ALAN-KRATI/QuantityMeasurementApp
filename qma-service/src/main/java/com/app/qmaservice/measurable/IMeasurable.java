package com.app.qmaservice.measurable;

public interface IMeasurable {

    double getConversionFactor();

    double convertToBase(double value);

    double convertFromBase(double value);

    String getUnitName();

    default void validOperationSupport(String operation) {
        throw new UnsupportedOperationException(
                operation + " is not supported for unit " + getUnitName()
        );
    }
}