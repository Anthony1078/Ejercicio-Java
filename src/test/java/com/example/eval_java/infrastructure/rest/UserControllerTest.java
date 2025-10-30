package com.example.eval_java.infrastructure.rest;

import com.example.eval_java.application.usecase.GetAllUsersUseCase;
import com.example.eval_java.application.usecase.RegisterUserUseCase;
import com.example.eval_java.domain.exception.EmailDuplicadoException;
import com.example.eval_java.infrastructure.persistence.IdempotencyService;
import com.example.eval_java.shared.dto.PhoneDto;
import com.example.eval_java.shared.dto.UserDto;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ApiExceptionHandler.class)
class UserControllerTest {

    @Resource
    private MockMvc mvc;

    @MockitoBean
    private RegisterUserUseCase register;

    @MockitoBean
    private GetAllUsersUseCase getAll;

    @MockitoBean
    private IdempotencyService idempotencyService;


    @Test
    void getAll_ok() throws Exception {
        var dto = new UserDto(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                "Juan Perez",
                "juan@nisum.com",
                Instant.now(), Instant.now(), Instant.now(),
                "0000000aaaa$$$$$$11111",
                true,
                List.of(new PhoneDto(
                        UUID.randomUUID(), "1234567", "1", "51"))
        );

        when(getAll.getAll()).thenReturn(List.of(dto));

        mvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data[0].email").value("juan@nisum.com"));
    }

    @Test
    void post_register_201() throws Exception {
        var dto = new UserDto(
                UUID.randomUUID(),
                "Juan Perez",
                "nuevo@nisum.com",
                Instant.now(), Instant.now(), Instant.now(),
                "jwt-token",
                true,
                List.of(new PhoneDto(
                        UUID.randomUUID(), "5555555", "1", "51"))
        );
        when(register.register(any())).thenReturn(dto);

        var body = """
        {
          "name":"Juan Perez",
          "email":"nuevo@nisum.com",
          "password":"Peru2025$$",
          "phones":[{"number":"5555555","citycode":"1","contrycode":"51"}]
        }""";

        mvc.perform(post("/api/v1/users")
                        .header("Idempotency-Key", "test-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.email").value("nuevo@nisum.com"));
    }

    @Test
    void post_register_duplicado_409() throws Exception {
        when(register.register(any())).thenThrow(new EmailDuplicadoException());

        var body = """
        {
          "name":"Juan Perez",
          "email":"juan@nisum.com",
          "password":"Peru2025$$",
          "phones":[{"number":"1234567","citycode":"1","contrycode":"51"}]
        }""";

        mvc.perform(post("/api/v1/users")
                        .header("Idempotency-Key", "dup-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").exists());
    }
}