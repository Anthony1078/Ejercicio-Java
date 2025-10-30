package com.example.eval_java.domain.exception;

public abstract class BusinessException extends RuntimeException {
    private final String code;
    private final int http;
    protected BusinessException(String msg, String code, int http) {
        super(msg); this.code = code; this.http = http;
    }
    public String code() { return code; }
    public int http() { return http; }
}
