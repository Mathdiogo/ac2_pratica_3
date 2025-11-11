package com.devops.projeto_ac2.domain.valueobjects;

import com.devops.projeto_ac2.domain.exceptions.InvalidValueObjectException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Value Object representando a média final do aluno
 * Encapsula regras de validação de nota
 */
@Getter
@EqualsAndHashCode
public class MediaFinal {
    
    private final double valor;
    
    private MediaFinal(double valor) {
        this.valor = valor;
    }
    
    /**
     * Factory method para criar uma média válida
     * @param valor a nota da média
     * @return uma instância válida de MediaFinal
     * @throws InvalidValueObjectException se o valor for inválido
     */
    public static MediaFinal criar(double valor) {
        validar(valor);
        return new MediaFinal(valor);
    }
    
    /**
     * Cria uma média inicial zerada
     */
    public static MediaFinal inicial() {
        return new MediaFinal(0.0);
    }
    
    private static void validar(double valor) {
        if (valor < 0.0) {
            throw new InvalidValueObjectException("Média final não pode ser negativa");
        }
        
        if (valor > 10.0) {
            throw new InvalidValueObjectException("Média final não pode ser maior que 10.0");
        }
    }
    
    /**
     * Verifica se o aluno foi aprovado (média >= 7.0)
     */
    public boolean aprovado() {
        return valor >= 7.0;
    }
    
    /**
     * Verifica se o aluno foi reprovado (média < 5.0)
     */
    public boolean reprovado() {
        return valor < 5.0;
    }
    
    /**
     * Verifica se o aluno está em recuperação (5.0 <= média < 7.0)
     */
    public boolean emRecuperacao() {
        return valor >= 5.0 && valor < 7.0;
    }
    
    @Override
    public String toString() {
        return String.format("%.2f", valor);
    }
}
