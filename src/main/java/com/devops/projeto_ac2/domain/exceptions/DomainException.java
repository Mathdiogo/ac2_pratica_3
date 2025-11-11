package com.devops.projeto_ac2.domain.exceptions;

/**
 * Exceção base para todas as exceções de domínio
 * Seguindo DDD, exceções de domínio devem ser explícitas
 */
public class DomainException extends RuntimeException {
    
    public DomainException(String message) {
        super(message);
    }
    
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
