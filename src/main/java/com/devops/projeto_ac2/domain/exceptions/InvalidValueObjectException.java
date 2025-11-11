package com.devops.projeto_ac2.domain.exceptions;

/**
 * Exceção lançada quando um Value Object recebe valor inválido
 */
public class InvalidValueObjectException extends DomainException {
    
    public InvalidValueObjectException(String message) {
        super(message);
    }
}
