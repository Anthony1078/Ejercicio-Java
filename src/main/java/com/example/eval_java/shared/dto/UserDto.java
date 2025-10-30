package com.example.eval_java.shared.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UserDto(
        UUID id,
        String name,
        String email,
        Instant created,
        Instant modified,
        Instant last_login,
        String token,
        boolean isactive,
        List<PhoneDto> phones
) {}
