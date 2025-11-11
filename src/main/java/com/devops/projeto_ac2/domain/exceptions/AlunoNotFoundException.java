package com.devops.projeto_ac2.domain.exceptions;

/**
 * Exceção lançada quando um aluno não é encontrado
 */
public class AlunoNotFoundException extends DomainException {
    
    public AlunoNotFoundException(Long id) {
        super("Aluno não encontrado com ID: " + id);
    }
    
    public AlunoNotFoundException(String message) {
        super(message);
    }
}
