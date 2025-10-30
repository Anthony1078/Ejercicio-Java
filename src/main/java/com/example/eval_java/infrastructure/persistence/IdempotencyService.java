package com.example.eval_java.infrastructure.persistence;


import com.example.eval_java.infrastructure.persistence.entity.IdempotencyRecord;
import com.example.eval_java.infrastructure.persistence.jpa.IdempotencyRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final IdempotencyRecordRepository repo;

    @Transactional(readOnly = true)
    public Optional<IdempotencyRecord> findByKey(String key) {
        return repo.findByKey(key);
    }

    @Transactional
    public IdempotencyRecord createPending(String key, String reqHash) {
        var now = Instant.now();
        var rec = IdempotencyRecord.builder()
                .id(UUID.randomUUID())
                .key(key)
                .requestHash(reqHash)
                .httpStatus(null)
                .created(now)
                .updated(now)
                .build();
        return repo.save(rec);
    }

    @Transactional
    public void storeResponse(String key, String reqHash, int status, String contentType, String body) {
        var rec = repo.findByKey(key).orElseThrow();
        if (!rec.getRequestHash().equals(reqHash)) return;

        rec.setHttpStatus(status);
        rec.setContentType(contentType);
        rec.setResponseBody(body);
        rec.setUpdated(Instant.now());
        repo.save(rec);
    }
}
