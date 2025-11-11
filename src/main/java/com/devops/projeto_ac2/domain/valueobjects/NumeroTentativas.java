package com.devops.projeto_ac2.domain.valueobjects;

import com.devops.projeto_ac2.domain.exceptions.InvalidValueObjectException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Value Object representando o número de tentativas de avaliação
 */
@Getter
@EqualsAndHashCode
public class NumeroTentativas {
    
    private final int valor;
    private static final int MAXIMO_TENTATIVAS = 3;
    
    private NumeroTentativas(int valor) {
        this.valor = valor;
    }
    
    /**
     * Cria uma nova instância de tentativas
     */
    public static NumeroTentativas criar(int valor) {
        validar(valor);
        return new NumeroTentativas(valor);
    }
    
    /**
     * Cria instância inicial (zero tentativas)
     */
    public static NumeroTentativas inicial() {
        return new NumeroTentativas(0);
    }
    
    private static void validar(int valor) {
        if (valor < 0) {
            throw new InvalidValueObjectException("Número de tentativas não pode ser negativo");
        }
        
        if (valor > MAXIMO_TENTATIVAS) {
            throw new InvalidValueObjectException(
                String.format("Número de tentativas não pode exceder %d", MAXIMO_TENTATIVAS)
            );
        }
    }
    
    /**
     * Incrementa o número de tentativas
     */
    public NumeroTentativas incrementar() {
        if (valor >= MAXIMO_TENTATIVAS) {
            throw new InvalidValueObjectException(
                "Limite máximo de tentativas atingido: " + MAXIMO_TENTATIVAS
            );
        }
        return new NumeroTentativas(valor + 1);
    }
    
    /**
     * Verifica se ainda há tentativas disponíveis
     */
    public boolean temTentativasDisponiveis() {
        return valor < MAXIMO_TENTATIVAS;
    }
    
    /**
     * Verifica se atingiu o limite de tentativas
     */
    public boolean atingiuLimite() {
        return valor >= MAXIMO_TENTATIVAS;
    }
    
    /**
     * Retorna quantas tentativas faltam
     */
    public int tentativasRestantes() {
        return MAXIMO_TENTATIVAS - valor;
    }
    
    @Override
    public String toString() {
        return String.format("%d/%d", valor, MAXIMO_TENTATIVAS);
    }
}
