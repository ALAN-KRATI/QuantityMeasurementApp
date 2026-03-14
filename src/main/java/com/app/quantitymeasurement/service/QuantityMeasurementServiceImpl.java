package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.measurable.IMeasurable;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.quantity.Quantity;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("unchecked")
@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    @Autowired
    private QuantityMeasurementRepository repository;

    @Override
    public QuantityMeasurementEntity compare(Quantity<?> q1, Quantity<?> q2) {
        try {

            boolean result = q1.equals(q2);

            QuantityMeasurementEntity entity =
                    new QuantityMeasurementEntity(
                            "COMPARE",
                            q1.toString(),
                            q2.toString(),
                            String.valueOf(result));

            repository.save(entity);

            return entity;

        } catch (Exception e) {

            QuantityMeasurementEntity entity =
                    new QuantityMeasurementEntity(e.getMessage());

            repository.save(entity);

            return entity;
        }
    }

   
    @Override
    public QuantityMeasurementEntity convert(Quantity<?> quantity, Object targetUnit) {
        try {
            Quantity<IMeasurable> q = (Quantity<IMeasurable>) quantity;
            Quantity<?> result = q.convertTo(((Quantity<?>) targetUnit).getUnit());

            QuantityMeasurementEntity entity =
                    new QuantityMeasurementEntity(
                            "CONVERT",
                            quantity.toString(),
                            null,
                            result.toString()
                    );

            repository.save(entity);
            return entity;

        } catch (Exception e) {

            QuantityMeasurementEntity entity =
                    new QuantityMeasurementEntity(e.getMessage());

            repository.save(entity);
            return entity;
        }
    }

    @Override
    public QuantityMeasurementEntity add(Quantity<?> q1, Quantity<?> q2) {
        try {

            Quantity result = ((Quantity) q1).add((Quantity) q2);

            QuantityMeasurementEntity entity =
                    new QuantityMeasurementEntity(
                            "ADD",
                            q1.toString(),
                            q2.toString(),
                            result.toString());

            repository.save(entity);

            return entity;

        } catch (Exception e) {

            QuantityMeasurementEntity entity =
                    new QuantityMeasurementEntity(e.getMessage());

            repository.save(entity);

            return entity;
        }
    }

    @Override
    public QuantityMeasurementEntity subtract(Quantity<?> q1, Quantity<?> q2) {
        try {

            Quantity result = ((Quantity) q1).subtract((Quantity) q2);

            QuantityMeasurementEntity entity =
                    new QuantityMeasurementEntity(
                            "SUBTRACT",
                            q1.toString(),
                            q2.toString(),
                            result.toString());

            repository.save(entity);

            return entity;

        } catch (Exception e) {

            QuantityMeasurementEntity entity =
                    new QuantityMeasurementEntity(e.getMessage());

            repository.save(entity);

            return entity;
        }
    }

    @Override
    public QuantityMeasurementEntity divide(Quantity<?> q1, Quantity<?> q2) {
        try {

            double result = ((Quantity) q1).divide((Quantity) q2);

            QuantityMeasurementEntity entity =
                    new QuantityMeasurementEntity(
                            "DIVIDE",
                            q1.toString(),
                            q2.toString(),
                            String.valueOf(result));

            repository.save(entity);

            return entity;

        } catch (Exception e) {

            QuantityMeasurementEntity entity =
                    new QuantityMeasurementEntity(e.getMessage());

            repository.save(entity);

            return entity;
        }
    }
}