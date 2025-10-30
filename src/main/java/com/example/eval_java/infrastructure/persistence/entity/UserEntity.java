package com.example.eval_java.infrastructure.persistence.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "uk_user_email", columnNames = "email"))
public class UserEntity {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable=false)
    private String name;
    @Column(nullable=false)
    private String email;
    @Column(nullable=false)
    private String passwordHash;
    @Column(nullable=false)
    private String token;
    @Column(nullable=false)
    private Boolean isActive;
    @Column(nullable=false)
    private Instant created;
    @Column(nullable=false)
    private Instant modified;
    @Column(nullable=false)
    private Instant lastLogin;

    @OneToMany(mappedBy="user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneEntity> phones = new ArrayList<>();
}
