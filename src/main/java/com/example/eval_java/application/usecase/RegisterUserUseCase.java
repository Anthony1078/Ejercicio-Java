package com.example.eval_java.application.usecase;

import com.example.eval_java.application.command.RegisterUserCommand;
import com.example.eval_java.shared.dto.UserDto;

public interface RegisterUserUseCase {
    UserDto register(RegisterUserCommand cmd);
}
