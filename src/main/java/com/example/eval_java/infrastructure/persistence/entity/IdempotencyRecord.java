package com.example.eval_java.infrastructure.persistence.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "idempotency_keys", uniqueConstraints = @UniqueConstraint(name = "uk_idemp_key", columnNames = "idem_key"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdempotencyRecord {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "idem_key", nullable = false, length = 64)
    private String key;

    @Column(name = "request_hash", nullable = false, length = 64)
    private String requestHash;

    @Lob
    @Column(name = "response_body")
    private String responseBody;

    @Column(name = "content_type", length = 255)
    private String contentType;

    @Column(name = "http_status")
    private Integer httpStatus;

    @Column(nullable = false)
    private Instant created;

    @Column(nullable = false)
    private Instant updated;

    @Version
    private Long version;
}
