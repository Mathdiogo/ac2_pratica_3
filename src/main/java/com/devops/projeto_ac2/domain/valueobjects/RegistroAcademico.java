package com.devops.projeto_ac2.domain.valueobjects;

import com.devops.projeto_ac2.domain.exceptions.InvalidValueObjectException;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

/**
 * Value Object representando o RA (Registro Acadêmico) do Aluno
 * Seguindo DDD: Value Objects são imutáveis e possuem validações
 */
@Embeddable
@Getter
@EqualsAndHashCode
public class RegistroAcademico {
    
    private String valor;
    
    // Construtor protegido para JPA
    protected RegistroAcademico() {}
    
    private RegistroAcademico(String valor) {
        this.valor = valor;
    }
    
    /**
     * Factory method para criar um RA válido
     * @param valor o número do RA
     * @return uma instância válida de RegistroAcademico
     * @throws InvalidValueObjectException se o valor for inválido
     */
    public static RegistroAcademico criar(String valor) {
        validar(valor);
        return new RegistroAcademico(valor);
    }
    
    private static void validar(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new InvalidValueObjectException("RA não pode ser nulo ou vazio");
        }
        
        if (valor.trim().length() < 5) {
            throw new InvalidValueObjectException("RA deve ter no mínimo 5 caracteres");
        }
        
        if (valor.trim().length() > 20) {
            throw new InvalidValueObjectException("RA deve ter no máximo 20 caracteres");
        }
        
        // Validar se contém apenas letras e números
        if (!valor.matches("^[a-zA-Z0-9]+$")) {
            throw new InvalidValueObjectException("RA deve conter apenas letras e números");
        }
    }
    
    @Override
    public String toString() {
        return valor;
    }
}
