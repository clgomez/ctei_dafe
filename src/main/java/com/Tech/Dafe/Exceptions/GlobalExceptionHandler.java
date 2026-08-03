package com.Tech.Dafe.Exceptions;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==========================
    // LOGIN CON CREDENCALES INVALIDAS
    // ==========================

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
        "codigo", "BAD_CREDENTIALS",
        "mensaje", "Credenciales inválidas"));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Map<String, String>> handleDisabled(DisabledException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
        "codigo", "USER_DISABLED",
        "mensaje", "Su cuenta no está activa"));
    }
    

    // ==========================
    // MANEJAR ERRORES GENERALES
    // ==========================          
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {
        ex.printStackTrace(); //para debug
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                       "codigo", "INTERNAL_ERROR",
                       "mensajes", List.of("Error interno del servidor. Intente más tarde")
                ));
    }


// ==========================
// Manejar errores de parseo (LocalDate, Enum, etc.)
// ==========================             -.
@ExceptionHandler(HttpMessageNotReadableException.class)
public ResponseEntity<?> handleInvalidFormat(HttpMessageNotReadableException ex) {

    List<String> errores = new java.util.ArrayList<>();

    Throwable cause = ex.getCause();

    if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException ife) {

        String fieldName = ife.getPath().get(0).getFieldName();

        switch (fieldName) {
            case "fechaNacimiento":
                errores.add("La fecha de nacimiento es inválida. Usa formato yyyy-MM-dd y una fecha real válida");
                break;

            case "genero":
                errores.add("El género es inválido o no permitido");
                break;

            case "ocupacion":
                errores.add("La ocupación es inválida o no permitida");
                break;

            case "tipoIdentificacion":
                errores.add("El tipo de identificación es inválido o no permitido");
                break;

            default:
                errores.add("Valor inválido en el campo: " + fieldName);
        }

    } else {
        errores.add("Error en el formato de los datos enviados");
    }

    return ResponseEntity.badRequest()
            .body(Map.of(
                    "codigo", "INVALID_FORMAT",
                    "mensajes", errores
            ));
}

   // ==========================
    // MANEJAR ERRORES DE NEGOCIO (GENÉRICOS)
    // ==========================
    /*@ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusiness(BusinessException e) {
        return ResponseEntity.badRequest()
                .body(e.getErrores()); // aquí está la clave
    }*/

   @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusiness(BusinessException e) {
        return ResponseEntity.badRequest().body(
            Map.of(
                "codigo", "BUSINESS_ERROR",
                "mensajes", e.getErrores()
                /*  .stream()
                    .map(CampoError::getMensaje)
                    .toList()*/
            )
        );
    }

    // ==========================
    // validar DTO
    // ==========================
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(
            org.springframework.web.bind.MethodArgumentNotValidException ex) {

        List<String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .toList();

        return ResponseEntity.badRequest()
                .body(Map.of(
            "codigo", "VALIDATION_ERROR",
            "mensajes", errores
        ));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "codigo", "NOT_FOUND",
                        "mensajes", List.of(ex.getMessage())
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {

        List<String> errores = List.of(ex.getMessage().split(";"));

        return ResponseEntity.badRequest()
                .body(Map.of(
                        "codigo", "VALIDATION_ERROR",
                        "mensajes", errores
                ));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSize(
            MaxUploadSizeExceededException ex) {

        return ResponseEntity.badRequest().body(
            Map.of(
                "codigo", "VALIDATION_ERROR",
                "mensajes", List.of(
                    "El archivo supera el tamaño máximo permitido de 10 MB."
                )
            )
        );
    }

}