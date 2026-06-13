package com.OsGuri.TrabalhoLucas.exception;

import com.OsGuri.TrabalhoLucas.exception.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    // 400 - Validação automática (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request){
        List<String> mensagens = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ":" + error.getDefaultMessage())
                .toList();

        ErrorResponseDto error = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                mensagens,
                request.getRequestURI(),
                LocalDateTime.now());

        return ResponseEntity.badRequest().body(error);

    }


    //404
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(
            RecursoNaoEncontradoException exception,
            HttpServletRequest request){

        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado",
                List.of(exception.getMessage()),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    //409
    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ErrorResponseDto> handleConflict(
            ConflitoException exception,
            HttpServletRequest request) {

        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                "Conflito",
                List.of(exception.getMessage()),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }


    // 400 regra de negócio
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErrorResponseDto> handleBusiness(
            RegraNegocioException ex,
            HttpServletRequest request) {

        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de regra de negócio",
                List.of(ex.getMessage()),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(error);
    }

    // 500 genérico
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno: " + ex.getClass().getName(),
                List.of(ex.getMessage() != null ? ex.getMessage() : "Sem mensagem"),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }




}
