package service;

import model.QuantityMeasurementEntity;
import quantity.Quantity;

@SuppressWarnings("unchecked")
public class QuantityMeasurementServiceImpl
        implements IQuantityMeasurementService {

    @Override
    public QuantityMeasurementEntity compare(Quantity<?> q1, Quantity<?> q2) {

        try {

            boolean result = q1.equals(q2);

            return new QuantityMeasurementEntity(
                    "COMPARE",
                    q1.toString(),
                    q2.toString(),
                    String.valueOf(result));

        } catch (Exception e) {

            return new QuantityMeasurementEntity(e.getMessage());
        }
    }

   @Override
    public QuantityMeasurementEntity convert(Quantity<?> quantity,
                                            Object targetUnit) {

        try {

            Quantity result =
                    ((Quantity) quantity).convertTo((measurable.IMeasurable) targetUnit);

            return new QuantityMeasurementEntity(
                    "CONVERT",
                    quantity.toString(),
                    result.toString());

        } catch (Exception e) {

            return new QuantityMeasurementEntity(e.getMessage());
        }
    }

    @Override
    public QuantityMeasurementEntity add(Quantity<?> q1, Quantity<?> q2) {

        try {

            Quantity result =
                    ((Quantity) q1).add((Quantity) q2);

            return new QuantityMeasurementEntity(
                    "ADD",
                    q1.toString(),
                    q2.toString(),
                    result.toString());

        } catch (Exception e) {

            return new QuantityMeasurementEntity(e.getMessage());
        }
    }

    @Override
    public QuantityMeasurementEntity subtract(Quantity<?> q1,
                                              Quantity<?> q2) {

        try {

            Quantity result =
                    ((Quantity) q1).subtract((Quantity) q2);

            return new QuantityMeasurementEntity(
                    "SUBTRACT",
                    q1.toString(),
                    q2.toString(),
                    result.toString());

        } catch (Exception e) {

            return new QuantityMeasurementEntity(e.getMessage());
        }
    }

    @Override
    public QuantityMeasurementEntity divide(Quantity<?> q1,
                                            Quantity<?> q2) {

        try {

            double result =
                    ((Quantity) q1).divide((Quantity) q2);

            return new QuantityMeasurementEntity(
                    "DIVIDE",
                    q1.toString(),
                    q2.toString(),
                    String.valueOf(result));

        } catch (Exception e) {

            return new QuantityMeasurementEntity(e.getMessage());
        }
    }
}