package com.app.qmaservice.service;

import com.app.qmaservice.dto.QuantityInputDTO;
import com.app.qmaservice.entity.QuantityMeasurementEntity;
import com.app.qmaservice.quantity.Quantity;
import com.app.qmaservice.repository.QuantityMeasurementRepository;
import com.app.qmaservice.util.QuantityModel;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class QuantityServiceImpl implements QuantityService {

    private final QuantityMeasurementRepository repository;
    private final RedisTemplate<String, String> redisTemplate;

    public QuantityServiceImpl(
            QuantityMeasurementRepository repository,
            RedisTemplate<String, String> redisTemplate
    ) {
        this.repository = repository;
        this.redisTemplate = redisTemplate;
    }

    private QuantityMeasurementEntity execute(
            String userEmail,
            String operation,
            Quantity<?> q1,
            Quantity<?> q2,
            Supplier<String> action
    ) {

        String cacheKey = operation + ":" + q1 + ":" + q2;

        String cachedResult = redisTemplate.opsForValue().get(cacheKey);

        if (cachedResult != null) {
            return new QuantityMeasurementEntity(
                    userEmail,
                    operation,
                    q1 != null ? q1.toString() : null,
                    q2 != null ? q2.toString() : null,
                    cachedResult + " (from Redis cache)"
            );
        }

        try {
            String result = action.get();

            redisTemplate.opsForValue().set(
                    cacheKey,
                    result,
                    Duration.ofMinutes(30)
            );

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    userEmail,
                    operation,
                    q1 != null ? q1.toString() : null,
                    q2 != null ? q2.toString() : null,
                    result
            );

            QuantityMeasurementEntity saved = repository.save(entity);

            // clear cached history because a new record was added
            redisTemplate.delete("history:" + userEmail);

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

        String historyKey = "history:" + userEmail;

        // just to check if Redis already has something
        String cachedHistory = redisTemplate.opsForValue().get(historyKey);

        if (cachedHistory != null) {
            System.out.println("History found in Redis cache. Redis really said 'I gotchu'.");
        }

        List<QuantityMeasurementEntity> history =
                repository.findByUserEmailOrderByCreatedAtDesc(userEmail);

        redisTemplate.opsForValue().set(
                historyKey,
                history.toString(),
                Duration.ofMinutes(10)
        );

        return history;
    }
}