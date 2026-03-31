package com.app.qmaservice.service;

import com.app.qmaservice.dto.QuantityInputDTO;
import com.app.qmaservice.entity.QuantityMeasurementEntity;
import com.app.qmaservice.quantity.Quantity;
import com.app.qmaservice.repository.QuantityMeasurementRepository;
import com.app.qmaservice.util.QuantityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class QuantityServiceImpl implements QuantityService {

    private final QuantityMeasurementRepository repository;

    public QuantityServiceImpl(
            QuantityMeasurementRepository repository
    ) {
        this.repository = repository;
    }

    private QuantityMeasurementEntity execute(
            String userEmail,
            String operation,
            Quantity<?> q1,
            Quantity<?> q2,
            Supplier<String> action
    ) {

       

       

        try {
            String result = action.get();

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    userEmail,
                    operation,
                    q1 != null ? q1.toString() : null,
                    q2 != null ? q2.toString() : null,
                    result
            );

            QuantityMeasurementEntity saved = repository.save(entity);

            return saved;

        } catch (Exception e) {

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    userEmail,
                    operation,
                    q1 != null ? q1.toString() : null,
                    q2 != null ? q2.toString() : null,
                    "ERROR: " + e.getMessage()
            );

            return repository.save(entity);
        }
    }

    @Override
    public QuantityMeasurementEntity compare(QuantityInputDTO input, String userEmail) {
        Quantity<?> q1 = QuantityModel.toQuantity(input.getThisQuantityDTO());
        Quantity<?> q2 = QuantityModel.toQuantity(input.getThatQuantityDTO());

        return execute(
                userEmail,
                "COMPARE",
                q1,
                q2,
                () -> String.valueOf(q1.equals(q2))
        );
    }

    @Override
    public QuantityMeasurementEntity convert(QuantityInputDTO input, String userEmail) {
        Quantity<?> q1 = QuantityModel.toQuantity(input.getThisQuantityDTO());
        Quantity<?> q2 = QuantityModel.toQuantity(input.getThatQuantityDTO());

        return execute(
                userEmail,
                "CONVERT",
                q1,
                q2,
                () -> ((Quantity) q1).convertTo(q2.getUnit()).toString()
        );
    }

    @Override
    public QuantityMeasurementEntity add(QuantityInputDTO input, String userEmail) {
        Quantity<?> q1 = QuantityModel.toQuantity(input.getThisQuantityDTO());
        Quantity<?> q2 = QuantityModel.toQuantity(input.getThatQuantityDTO());

        return execute(
                userEmail,
                "ADD",
                q1,
                q2,
                () -> ((Quantity) q1).add((Quantity) q2).toString()
        );
    }

    @Override
    public QuantityMeasurementEntity subtract(QuantityInputDTO input, String userEmail) {
        Quantity<?> q1 = QuantityModel.toQuantity(input.getThisQuantityDTO());
        Quantity<?> q2 = QuantityModel.toQuantity(input.getThatQuantityDTO());

        return execute(
                userEmail,
                "SUBTRACT",
                q1,
                q2,
                () -> ((Quantity) q1).subtract((Quantity) q2).toString()
        );
    }

    @Override
    public QuantityMeasurementEntity divide(QuantityInputDTO input, String userEmail) {
        Quantity<?> q1 = QuantityModel.toQuantity(input.getThisQuantityDTO());
        Quantity<?> q2 = QuantityModel.toQuantity(input.getThatQuantityDTO());

        return execute(
                userEmail,
                "DIVIDE",
                q1,
                q2,
                () -> String.valueOf(((Quantity) q1).divide((Quantity) q2))
        );
    }

    @Override
    public List<QuantityMeasurementEntity> getHistory(String userEmail) {



        List<QuantityMeasurementEntity> history =
                repository.findByUserEmailOrderByCreatedAtDesc(userEmail);



        return history;
    }
}