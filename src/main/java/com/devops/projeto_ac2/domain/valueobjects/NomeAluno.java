package com.devops.projeto_ac2.domain.valueobjects;

import com.devops.projeto_ac2.domain.exceptions.InvalidValueObjectException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Value Object representando o nome do aluno
 * Encapsula regras de validação de nome
 */
@Getter
@EqualsAndHashCode
public class NomeAluno {
    
    private final String valor;
    
    private NomeAluno(String valor) {
        this.valor = valor;
    }
    
    /**
     * Factory method para criar um nome válido
     * @param valor o nome completo
     * @return uma instância válida de NomeAluno
     * @throws InvalidValueObjectException se o valor for inválido
     */
    public static NomeAluno criar(String valor) {
        validar(valor);
        return new NomeAluno(valor.trim());
    }
    
    private static void validar(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new InvalidValueObjectException("Nome não pode ser nulo ou vazio");
        }
        
        if (valor.trim().length() < 3) {
            throw new InvalidValueObjectException("Nome deve ter no mínimo 3 caracteres");
        }
        
        if (valor.trim().length() > 100) {
            throw new InvalidValueObjectException("Nome deve ter no máximo 100 caracteres");
        }
        
        // Validar se contém apenas letras e espaços
        if (!valor.matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
            throw new InvalidValueObjectException("Nome deve conter apenas letras e espaços");
        }
    }
    
    /**
     * Retorna o primeiro nome do aluno
     */
    public String primeiroNome() {
        return valor.split(" ")[0];
    }
    
    @Override
    public String toString() {
        return valor;
    }
}
