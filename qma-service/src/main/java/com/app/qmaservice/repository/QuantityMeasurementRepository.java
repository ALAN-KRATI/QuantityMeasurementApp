package com.app.qmaservice.repository;

import com.app.qmaservice.entity.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long> {

    List<QuantityMeasurementEntity> findByUserEmailOrderByCreatedAtDesc(String userEmail);

    List<QuantityMeasurementEntity> findByOperation(String operation);

    long countByOperation(String operation);
}