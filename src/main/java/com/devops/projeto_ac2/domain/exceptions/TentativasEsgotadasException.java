package com.devops.projeto_ac2.domain.exceptions;

/**
 * Exceção lançada quando aluno tenta realizar ação sem tentativas disponíveis
 */
public class TentativasEsgotadasException extends DomainException {
    
    public TentativasEsgotadasException() {
        super("Aluno já utilizou todas as tentativas disponíveis");
    }
    
    public TentativasEsgotadasException(String message) {
        super(message);
    }
}
