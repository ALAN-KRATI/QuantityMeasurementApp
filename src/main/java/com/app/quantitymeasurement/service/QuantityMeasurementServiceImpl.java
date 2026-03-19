package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.measurable.IMeasurable;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.quantity.Quantity;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@SuppressWarnings("unchecked")
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private final QuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(QuantityMeasurementRepository repository) {
        this.repository = repository;
    }

    private QuantityMeasurementEntity execute(String operation, String input1, String input2, Supplier<String> action) {
        try {
            String result = action.get();
            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(operation, input1, input2, result);

            repository.save(entity);
            return entity;

        } catch (Exception e) {
            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(e.getMessage());
            repository.save(entity);
            return entity;
        }
    }

    @Override
    public QuantityMeasurementEntity compare(Quantity<?> q1, Quantity<?> q2) {
        return execute(
                "COMPARE",
                q1.toString(),
                q2.toString(),
                () -> String.valueOf(q1.equals(q2))
        );
    }

    @Override
    public QuantityMeasurementEntity convert(Quantity<?> q1, Quantity<?> q2) {
        return execute(
                "CONVERT",
                q1.toString(),
                null,
                () -> {
                    Quantity<?> result = ((Quantity) q1).convertTo(q2.getUnit());
                    return result.toString();
                }
        );
    }

    @Override
    public QuantityMeasurementEntity add(Quantity<?> q1, Quantity<?> q2) {
        return execute(
                "ADD",
                q1.toString(),
                q2.toString(),
                () -> ((Quantity) q1).add((Quantity) q2).toString()
        );
    }

    @Override
    public QuantityMeasurementEntity subtract(Quantity<?> q1, Quantity<?> q2) {
        return execute(
                "SUBTRACT",
                q1.toString(),
                q2.toString(),
                () -> ((Quantity) q1).subtract((Quantity) q2).toString()
        );
    }

    @Override
    public QuantityMeasurementEntity divide(Quantity<?> q1, Quantity<?> q2) {
        return execute(
                "DIVIDE",
                q1.toString(),
                q2.toString(),
                () -> String.valueOf(((Quantity) q1).divide((Quantity) q2))
        );
    }
}