package com.example.eval_java.infrastructure.rest;

import com.example.eval_java.domain.exception.EmailDuplicadoException;
import com.example.eval_java.shared.dto.ResponseDTO;
import com.example.eval_java.shared.enums.Status;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static ResponseEntity<ResponseDTO<Void>> wrap(HttpStatus status, String message) {
        var body = new ResponseDTO<Void>(Status.ERROR.name(), message, null, Instant.now().toString());
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(EmailDuplicadoException.class)
    public ResponseEntity<ResponseDTO<Void>> handleEmailDuplicado(EmailDuplicadoException ex) {
        return wrap(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));
        return wrap(HttpStatus.BAD_REQUEST, msg.isBlank() ? "Datos inválidos" : msg);
    }
    private String formatFieldError(FieldError fe) {
        return fe.getField() + ": " + (fe.getDefaultMessage() == null ? "inválido" : fe.getDefaultMessage());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Void>> handleAll(Exception ex) {
        return wrap(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno");
    }
}