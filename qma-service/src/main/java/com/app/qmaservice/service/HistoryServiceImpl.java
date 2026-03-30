package com.app.qmaservice.service;

import com.app.qmaservice.dto.HistoryResponse;
import com.app.qmaservice.entity.QuantityMeasurementEntity;
import com.app.qmaservice.repository.QuantityMeasurementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final QuantityMeasurementRepository repository;

    public HistoryServiceImpl(QuantityMeasurementRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<HistoryResponse> getHistory(String userEmail) {

        List<QuantityMeasurementEntity> history =
                repository.findByUserEmailOrderByCreatedAtDesc(userEmail);

        return history.stream()
                .map(entity -> new HistoryResponse(
                        entity.getId(),
                        entity.getOperation(),
                        entity.getResult(),
                        entity.getCreatedAt()
                ))
                .toList();
    }
}