package com.example.eval_java.infrastructure.persistence.jpa;

import com.example.eval_java.infrastructure.persistence.entity.IdempotencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IdempotencyRecordRepository extends JpaRepository<IdempotencyRecord, UUID> {
    Optional<IdempotencyRecord> findByKey(String key);
}