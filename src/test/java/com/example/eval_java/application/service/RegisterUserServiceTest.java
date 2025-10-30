package com.example.eval_java.application.service;

import com.example.eval_java.application.command.RegisterUserCommand;
import com.example.eval_java.domain.exception.EmailDuplicadoException;
import com.example.eval_java.domain.repository.UserRepository;
import com.example.eval_java.infrastructure.persistence.entity.UserEntity;
import com.example.eval_java.infrastructure.security.JwtService;
import com.example.eval_java.shared.dto.PhoneRequest;
import com.example.eval_java.shared.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterUserServiceTest {

    private UserRepository users;
    private PasswordEncoder encoder;
    private JwtService jwt;
    private RegisterUserService service;

    @BeforeEach
    void setUp() {
        users = mock(UserRepository.class);
        encoder = mock(PasswordEncoder.class);
        jwt = mock(JwtService.class);
        service = new RegisterUserService(users, encoder, jwt);
    }

    @Test
    void register_ok() {
        var cmd = new RegisterUserCommand(
                "Juan Perez", "juan@nisum.com", "Peru2025$$",
                List.of(new PhoneRequest("987654321", "1", "51"))
        );

        when(users.existsByEmail("juan@nisum.com")).thenReturn(false);
        when(encoder.encode("Peru2025$$")).thenReturn("hashed-pass");
        when(jwt.issueFor("juan@nisum.com")).thenReturn("jwt-token");

        var saved = new UserEntity();
        saved.setId(UUID.randomUUID());
        saved.setName("Juan Perez");
        saved.setEmail("juan@nisum.com");
        saved.setPasswordHash("hashed-pass");
        saved.setToken("jwt-token");
        saved.setIsActive(true);
        saved.setCreated(Instant.now());
        saved.setModified(Instant.now());
        saved.setLastLogin(Instant.now());
        when(users.save(any(UserEntity.class))).thenReturn(saved);

        UserDto dto = service.register(cmd);

        assertThat(dto.email()).isEqualTo("juan@nisum.com");
        verify(users).save(any(UserEntity.class));
        verify(jwt).issueFor("juan@nisum.com");
    }

    @Test
    void register_emailDuplicado_lanza409() {
        var cmd = new RegisterUserCommand("Juan", "dup@nisum.com", "Peru2025$$", List.of());
        when(users.existsByEmail("dup@nisum.com")).thenReturn(true);

        assertThatThrownBy(() -> service.register(cmd))
                .isInstanceOf(EmailDuplicadoException.class);

        verify(users, never()).save(any());
    }
}