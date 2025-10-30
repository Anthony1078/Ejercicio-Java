package com.example.eval_java.application.service;

import com.example.eval_java.application.usecase.GetAllUsersUseCase;
import com.example.eval_java.domain.repository.UserRepository;
import com.example.eval_java.infrastructure.mapper.UserMapper;
import com.example.eval_java.shared.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetAllUsersService implements GetAllUsersUseCase {

    private final UserRepository users;
    private final UserMapper mapper = new UserMapper();

    public GetAllUsersService(UserRepository users) {
        this.users = users;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        return users.findAll().stream().map(mapper::toDto).toList();
    }
}