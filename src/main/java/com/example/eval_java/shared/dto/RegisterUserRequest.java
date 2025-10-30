package com.example.eval_java.shared.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.util.List;

@Schema(name = "RegisterUserRequest", description = "Payload de registro de usuario")
public record RegisterUserRequest(
        @Schema(description = "Nombre completo", example = "Juan Perez")
        @NotBlank String name,

        @Schema(description = "Correo único del usuario", example = "juan@nisum.com")
        @NotBlank @Email String email,

        @Schema(
                description = "Contraseña con mayúscula, minúscula y dígito (mínimo 8)",
                example = "Peru2025$",
                pattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$")
        @NotBlank @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$")
        String password,

        @Schema(description = "Lista de teléfonos")
        @NotEmpty List<PhoneRequest> phones
) {}