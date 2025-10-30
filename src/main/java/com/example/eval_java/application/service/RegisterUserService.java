package com.example.eval_java.application.service;


import com.example.eval_java.application.command.RegisterUserCommand;
import com.example.eval_java.application.usecase.RegisterUserUseCase;
import com.example.eval_java.domain.exception.EmailDuplicadoException;
import com.example.eval_java.domain.repository.UserRepository;
import com.example.eval_java.infrastructure.mapper.UserMapper;
import com.example.eval_java.infrastructure.security.JwtService;
import com.example.eval_java.shared.dto.RegisterUserRequest;
import com.example.eval_java.shared.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    private final UserMapper mapper = new UserMapper();

    public RegisterUserService(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users;
        this.encoder = encoder; this.jwt = jwt;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserDto register(RegisterUserCommand cmd) {
        log.info("Iniciando registro para email={}", cmd.email());

        if (users.existsByEmail(cmd.email())) {
            log.warn("Email duplicado detectado: {}", cmd.email());
            throw new EmailDuplicadoException();
        }

        var req = new RegisterUserRequest(cmd.name(), cmd.email(), cmd.password(), cmd.phones());
        var now = Instant.now();
        var token = jwt.issueFor(req.email().toLowerCase());
        var encodedPass = encoder.encode(req.password());

        log.info("Token generado={} Pass hash={}", token, encodedPass);

        var entity = mapper.toEntity(req, encodedPass, token);
        entity.setCreated(now);
        entity.setModified(now);
        entity.setLastLogin(now);

        try {
            var saved = users.save(entity);
            log.info("Usuario persistido con ID={}", saved.getId());
            return mapper.toDto(saved);
        } catch (Exception e) {
            log.error("Error al guardar usuario", e);
            throw e;
        }
    }
}
