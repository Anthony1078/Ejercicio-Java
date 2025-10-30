package com.example.eval_java.shared.dto;

import java.util.UUID;

public record PhoneDto(UUID id, String number, String citycode, String contrycode) {}
