package com.devops.projeto_ac2.infrastructure.web.exception;

import com.devops.projeto_ac2.domain.exceptions.AlunoNotFoundException;
import com.devops.projeto_ac2.domain.exceptions.DomainException;
import com.devops.projeto_ac2.domain.exceptions.InvalidValueObjectException;
import com.devops.projeto_ac2.domain.exceptions.TentativasEsgotadasException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler global para tratamento de exceções
 * Garante respostas padronizadas para todas as exceções da aplicação
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Trata exceções de aluno não encontrado
     */
    @ExceptionHandler(AlunoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAlunoNotFound(
            AlunoNotFoundException ex, 
            HttpServletRequest request) {
        
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Aluno não encontrado")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    /**
     * Trata exceções de Value Object inválido
     */
    @ExceptionHandler(InvalidValueObjectException.class)
    public ResponseEntity<ErrorResponse> handleInvalidValueObject(
            InvalidValueObjectException ex, 
            HttpServletRequest request) {
        
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Dados inválidos")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    /**
     * Trata exceções de tentativas esgotadas
     */
    @ExceptionHandler(TentativasEsgotadasException.class)
    public ResponseEntity<ErrorResponse> handleTentativasEsgotadas(
            TentativasEsgotadasException ex, 
            HttpServletRequest request) {
        
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Tentativas esgotadas")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    
    /**
     * Trata exceções gerais de domínio
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(
            DomainException ex, 
            HttpServletRequest request) {
        
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Erro de regra de negócio")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    /**
     * Trata erros de validação (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        
        List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>();
        
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.add(ErrorResponse.FieldError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build());
        }
        
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Erro de validação")
                .message("Um ou mais campos estão inválidos")
                .path(request.getRequestURI())
                .fieldErrors(fieldErrors)
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    /**
     * Trata exceções genéricas não previstas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, 
            HttpServletRequest request) {
        
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Erro interno do servidor")
                .message("Ocorreu um erro inesperado. Contate o suporte.")
                .path(request.getRequestURI())
                .build();
        
        // Log da exceção para debug (importante para produção)
        ex.printStackTrace();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
