package com.example.eval_java.infrastructure.persistence;

import com.example.eval_java.domain.repository.UserRepository;
import com.example.eval_java.infrastructure.persistence.entity.UserEntity;
import com.example.eval_java.infrastructure.persistence.jpa.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    private final JpaUserRepository jpa;
    public boolean existsByEmail(String email){ return jpa.existsByEmailIgnoreCase(email); }
    public UserEntity save(UserEntity e){ return jpa.save(e); }
    public List<UserEntity> findAll(){ return jpa.findAll(); }
}