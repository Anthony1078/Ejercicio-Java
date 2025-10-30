package com.example.eval_java.application.usecase;

import com.example.eval_java.shared.dto.UserDto;
import java.util.List;

public interface GetAllUsersUseCase {
    List<UserDto> getAll();
}
