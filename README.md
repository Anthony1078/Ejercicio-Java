# Proyecto Eval-Java

## Descripción general
Este proyecto implementa una **API RESTful en Spring Boot (Java 17)** para la **gestión de usuarios**, simulando un sistema de registro y consulta con validaciones de seguridad, idempotencia y persistencia en memoria (**H2 Database**).

Incluye:
- Control de idempotencia por encabezado HTTP.
- Validaciones de negocio y manejo de excepciones personalizadas.
- Documentación automática con **Swagger UI**.
- Pruebas unitarias y de integración con **JUnit 5** y **Mockito**.
- Reportes de cobertura generados con **JaCoCo**.

---

## Arquitectura
El proyecto sigue una estructura modular inspirada en los principios de **Clean Architecture** y **Hexagonal Architecture**, con una clara separación entre dominio, aplicación e infraestructura.

## Patrones implementados
- **Clean Architecture / Hexagonal:** capas desacopladas y modulares.
- **DTO Pattern:** transferencia de datos entre capas.
- **Repository Pattern:** persistencia desacoplada.
- **Mapper Pattern:** transformación de entidades ↔ DTOs.
- **AOP Pattern:** para la gestión de idempotencia.
- **Record Pattern (Java 17):** modelos inmutables y simplificados.
- **Builder Pattern:** creación controlada de entidades y DTOs con código más legible y seguro.

---

## Base de datos (H2)
Se utiliza **H2 Database en memoria**.  
Los datos se inicializan automáticamente mediante `data.sql`.

**Consola H2:**  
[http://localhost:8080/h2-console](http://localhost:8080/h2-console)


## Documentación Swagger
La API cuenta con documentación interactiva generada automáticamente mediante **Swagger UI**.

**URL:**  
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

**Credenciales de acceso:**  

Usuario: admin

Contraseña: admin123



## Requisitos previos
Para ejecutar el proyecto localmente se necesita:
- **Java 17** o superior
- **Gradle 8.x**
- Puerto **8080** disponible

---

## Ejecución del proyecto