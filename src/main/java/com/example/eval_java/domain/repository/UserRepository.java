package com.example.eval_java.domain.repository;

import com.example.eval_java.infrastructure.persistence.entity.UserEntity;

import java.util.List;

public interface UserRepository {
    boolean existsByEmail(String email);
    UserEntity save(UserEntity e);
    List<UserEntity> findAll();
}
