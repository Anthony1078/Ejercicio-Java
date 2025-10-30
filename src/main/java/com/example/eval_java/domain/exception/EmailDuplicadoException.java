package com.example.eval_java.domain.exception;

import com.example.eval_java.shared.util.ApiConstants;

public class EmailDuplicadoException extends BusinessException {
    public EmailDuplicadoException() { super(ApiConstants.MSG_EMAIL_DUP, "EMAIL_DUP", 409); }
}
