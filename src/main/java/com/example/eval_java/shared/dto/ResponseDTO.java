package com.example.eval_java.shared.dto;

public record ResponseDTO<T>(String status, String message, T data, String timestamp) {}
