package com.example.eval_java.application.command;

import com.example.eval_java.shared.dto.PhoneRequest;

import java.util.List;

public record RegisterUserCommand(String name, String email, String password, List<PhoneRequest> phones) {}

