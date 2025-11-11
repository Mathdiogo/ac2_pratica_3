package com.devops.projeto_ac2.domain.valueobjects;

import com.devops.projeto_ac2.domain.exceptions.InvalidValueObjectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes unitários para NumeroTentativas (Value Object)
 */
@DisplayName("Testes do Value Object NumeroTentativas")
class NumeroTentativasTest {
    
    @Test
    @DisplayName("Deve criar número de tentativas válido")
    void deveCriarNumeroTentativasValido() {
        // Act
        NumeroTentativas tentativas = NumeroTentativas.criar(2);
        
        // Assert
        assertThat(tentativas).isNotNull();
        assertThat(tentativas.getValor()).isEqualTo(2);
    }
    
    @Test
    @DisplayName("Deve criar tentativas inicial zerada")
    void deveCriarTentativasInicial() {
        // Act
        NumeroTentativas tentativas = NumeroTentativas.inicial();
        
        // Assert
        assertThat(tentativas.getValor()).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando tentativas é negativa")
    void deveLancarExcecaoQuandoTentativasNegativa() {
        // Act & Assert
        assertThatThrownBy(() -> NumeroTentativas.criar(-1))
                .isInstanceOf(InvalidValueObjectException.class)
                .hasMessage("Número de tentativas não pode ser negativo");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando tentativas excede máximo (3)")
    void deveLancarExcecaoQuandoExcedeMaximo() {
        // Act & Assert
        assertThatThrownBy(() -> NumeroTentativas.criar(4))
                .isInstanceOf(InvalidValueObjectException.class)
                .hasMessageContaining("não pode exceder 3");
    }
    
    @Test
    @DisplayName("Deve incrementar tentativas corretamente")
    void deveIncrementarTentativas() {
        // Arrange
        NumeroTentativas tentativas = NumeroTentativas.criar(1);
        
        // Act
        NumeroTentativas incrementada = tentativas.incrementar();
        
        // Assert
        assertThat(incrementada.getValor()).isEqualTo(2);
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao tentar incrementar quando já está no limite")
    void deveLancarExcecaoAoIncrementarNoLimite() {
        // Arrange
        NumeroTentativas tentativas = NumeroTentativas.criar(3);
        
        // Act & Assert
        assertThatThrownBy(() -> tentativas.incrementar())
                .isInstanceOf(InvalidValueObjectException.class)
                .hasMessageContaining("Limite máximo de tentativas atingido");
    }
    
    @Test
    @DisplayName("Deve verificar se tem tentativas disponíveis")
    void deveVerificarTentativasDisponiveis() {
        // Arrange
        NumeroTentativas comDisponivel = NumeroTentativas.criar(2);
        NumeroTentativas semDisponivel = NumeroTentativas.criar(3);
        
        // Assert
        assertThat(comDisponivel.temTentativasDisponiveis()).isTrue();
        assertThat(semDisponivel.temTentativasDisponiveis()).isFalse();
    }
    
    @Test
    @DisplayName("Deve verificar se atingiu limite")
    void deveVerificarSeLimiteAtingido() {
        // Arrange
        NumeroTentativas naoAtingiu = NumeroTentativas.criar(2);
        NumeroTentativas atingiu = NumeroTentativas.criar(3);
        
        // Assert
        assertThat(naoAtingiu.atingiuLimite()).isFalse();
        assertThat(atingiu.atingiuLimite()).isTrue();
    }
    
    @Test
    @DisplayName("Deve calcular tentativas restantes corretamente")
    void deveCalcularTentativasRestantes() {
        // Arrange
        NumeroTentativas tentativas = NumeroTentativas.criar(1);
        
        // Assert
        assertThat(tentativas.tentativasRestantes()).isEqualTo(2);
    }
    
    @Test
    @DisplayName("Deve formatar toString corretamente")
    void deveFormatarToString() {
        // Arrange
        NumeroTentativas tentativas = NumeroTentativas.criar(2);
        
        // Assert
        assertThat(tentativas.toString()).isEqualTo("2/3");
    }
}
