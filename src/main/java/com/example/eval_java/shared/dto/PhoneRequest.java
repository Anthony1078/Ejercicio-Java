package com.example.eval_java.shared.dto;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PhoneRequest", description = "Teléfono del usuario")
public record PhoneRequest(
        @Schema(example = "1234567") @NotBlank String number,
        @Schema(example = "1")       @NotBlank String citycode,
        @Schema(example = "51")      @NotBlank String contrycode
) {}