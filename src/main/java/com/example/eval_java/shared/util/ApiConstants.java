package com.example.eval_java.shared.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiConstants {

    public static final String API_V1_USERS = "/api/v1/users";

    public static final String HEADER_IDEMPOTENCY_KEY = "Idempotency-Key";

    public static final String MSG_USER_CREATED = "Usuario creado";
    public static final String MSG_USER_LIST = "Lista de usuarios";
    public static final String MSG_EMAIL_DUP = "El correo ya se encuentra registrado";

}
