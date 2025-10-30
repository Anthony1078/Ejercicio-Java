package com.example.eval_java.application.service;

import com.example.eval_java.domain.repository.UserRepository;
import com.example.eval_java.infrastructure.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GetAllUsersServiceTest {

    private UserRepository users;
    private GetAllUsersService service;

    @BeforeEach
    void setUp() {
        users = mock(UserRepository.class);
        service = new GetAllUsersService(users);
    }

    @Test
    void getAll_mapeaDto() {
        var e = new UserEntity();
        e.setId(UUID.randomUUID());
        e.setName("Juan Perez");
        e.setEmail("juan@nisum.com");
        e.setIsActive(true);
        e.setCreated(Instant.now());
        e.setModified(Instant.now());
        e.setLastLogin(Instant.now());

        when(users.findAll()).thenReturn(List.of(e));

        var out = service.getAll();
        assertThat(out).hasSize(1);
        assertThat(out.get(0).email()).isEqualTo("juan@nisum.com");
    }
}