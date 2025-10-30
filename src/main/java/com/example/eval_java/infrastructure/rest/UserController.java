package com.example.eval_java.infrastructure.rest;

import com.example.eval_java.application.command.RegisterUserCommand;
import com.example.eval_java.application.usecase.GetAllUsersUseCase;
import com.example.eval_java.application.usecase.RegisterUserUseCase;
import com.example.eval_java.shared.dto.*;
import com.example.eval_java.shared.enums.Status;
import com.example.eval_java.shared.util.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(ApiConstants.API_V1_USERS)
@Tag(name = "Users", description = "Operaciones de registro y consulta de usuarios")
public class UserController {

    private final RegisterUserUseCase register;
    private final GetAllUsersUseCase getAllUsers;

    public UserController(RegisterUserUseCase register, GetAllUsersUseCase getAllUsers) {
        this.register = register;
        this.getAllUsers = getAllUsers;
    }

    @Operation(
            summary = "Registrar usuario",
            description = """
            Crea un usuario nuevo validando email único y formato de contraseña.
            **Idempotencia:** Envíe `Idempotency-Key` (UUID o string único) para que
            reintentos sean seguros. Si la misma clave se reusa con cuerpo diferente,
            se retorna **409 IDEMPOTENCY_CONFLICT**.
            """,
            parameters = {
                    @Parameter(
                            name = "Idempotency-Key",
                            description = "Clave única por intento de creación (requerido para idempotencia).",
                            required = true,
                            in = ParameterIn.HEADER,
                            example = "6f8a3c8f-1db4-4a6f-9e9a-7d8c4f0b1c2e"
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = ApiConstants.MSG_USER_CREATED,
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                {
                  "status":"SUCCESS",
                  "message":"Usuario creado",
                  "data":{
                    "id":"00000000-0000-000000000001",
                    "name":"Juan Perez",
                    "email":"juan@nisum.com",
                    "phones":[ { "number":"1234567","citycode":"1","contrycode":"51" } ],
                    "created":"2025-10-29T00:00:00Z",
                    "modified":"2025-10-29T00:00:00Z",
                    "lastLogin":"2025-10-29T00:00:00Z",
                    "token":"0000000aaaa$$$$$$11111",
                    "isactive": true
                  },
                  "timestamp":"2025-10-30T00:00:00Z"
                }""")
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o falta Idempotency-Key",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                {
                  "status":"ERROR",
                  "message":"Email: debe ser una dirección de correo electrónico válida; Password: debe coincidir con el patrón",
                  "data":null,
                  "timestamp":"2025-10-30T00:00:00Z"
                }""")
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Conflicto (Email duplicado o Idempotency-Key con payload distinto)",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name="EMAIL_DUP", value = """
                    { "status":"ERROR","message":"El correo ya registrado","data":null,"timestamp":"2025-10-30T00:00:00Z" }"""),
                                    @ExampleObject(name="IDEMPOTENCY_CONFLICT", value = """
                    { "status":"ERROR","message":"Idempotency-Key ya usada con otro contenido","data":null,"timestamp":"2025-10-30T00:00:00Z" }""")
                            }
                    )
            )
    })
    @PostMapping
    public ResponseEntity<ResponseDTO<UserDto>> register(
            @RequestHeader(ApiConstants.HEADER_IDEMPOTENCY_KEY) String idempotencyKey,
            @Valid @RequestBody RegisterUserRequest req) {

        var cmd  = new RegisterUserCommand(req.name(), req.email(), req.password(), req.phones());
        var dto  = register.register(cmd);
        var body = new ResponseDTO<>(Status.SUCCESS.name(), "Usuario creado", dto, Instant.now().toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }


    @Operation(summary = "Listar usuarios", description = "Retorna todos los usuarios registrados.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
            { "status":"SUCCESS", "message":"Lista de usuarios",
              "data":[
                { "id":"00000000-0000-000000000001",
                  "name":"Juan Perez", "email":"juan@nisum.com",
                  "phones":[{ "number":"1234567","citycode":"1","contrycode":"51" }],
                  "created":"2025-10-29T00:00:00Z","modified":"2025-10-29T00:00:00Z",
                  "lastLogin":"2025-10-29T00:00:00Z","token":"0000000aaaa$$$$$$11111","isactive":true
                }
              ],
              "timestamp":"2025-10-30T00:00:00Z" }""")
            )
    )
    @GetMapping
    public ResponseEntity<ResponseDTO<List<UserDto>>> getAll() {
        var data = getAllUsers.getAll();
        var body = new ResponseDTO<>(Status.SUCCESS.name(), ApiConstants.MSG_USER_LIST , data, Instant.now().toString());
        return ResponseEntity.ok(body);
    }
}