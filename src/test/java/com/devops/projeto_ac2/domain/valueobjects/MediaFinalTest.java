package com.devops.projeto_ac2.domain.valueobjects;

import com.devops.projeto_ac2.domain.exceptions.InvalidValueObjectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes unitários para MediaFinal (Value Object)
 */
@DisplayName("Testes do Value Object MediaFinal")
class MediaFinalTest {
    
    @Test
    @DisplayName("Deve criar média válida com sucesso")
    void deveCriarMediaValida() {
        // Arrange & Act
        MediaFinal media = MediaFinal.criar(8.5);
        
        // Assert
        assertThat(media).isNotNull();
        assertThat(media.getValor()).isEqualTo(8.5);
    }
    
    @Test
    @DisplayName("Deve criar média inicial zerada")
    void deveCriarMediaInicial() {
        // Act
        MediaFinal media = MediaFinal.inicial();
        
        // Assert
        assertThat(media.getValor()).isEqualTo(0.0);
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando média é negativa")
    void deveLancarExcecaoQuandoMediaNegativa() {
        // Act & Assert
        assertThatThrownBy(() -> MediaFinal.criar(-1.0))
                .isInstanceOf(InvalidValueObjectException.class)
                .hasMessage("Média final não pode ser negativa");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando média é maior que 10")
    void deveLancarExcecaoQuandoMediaMaiorQue10() {
        // Act & Assert
        assertThatThrownBy(() -> MediaFinal.criar(10.5))
                .isInstanceOf(InvalidValueObjectException.class)
                .hasMessage("Média final não pode ser maior que 10.0");
    }
    
    @Test
    @DisplayName("Deve aceitar média 0.0")
    void deveAceitarMediaZero() {
        // Act
        MediaFinal media = MediaFinal.criar(0.0);
        
        // Assert
        assertThat(media.getValor()).isEqualTo(0.0);
    }
    
    @Test
    @DisplayName("Deve aceitar média 10.0")
    void deveAceitarMediaDez() {
        // Act
        MediaFinal media = MediaFinal.criar(10.0);
        
        // Assert
        assertThat(media.getValor()).isEqualTo(10.0);
    }
    
    @Test
    @DisplayName("Deve identificar aluno aprovado (média >= 7.0)")
    void deveIdentificarAlunoAprovado() {
        // Arrange
        MediaFinal media = MediaFinal.criar(7.5);
        
        // Assert
        assertThat(media.aprovado()).isTrue();
        assertThat(media.reprovado()).isFalse();
        assertThat(media.emRecuperacao()).isFalse();
    }
    
    @Test
    @DisplayName("Deve identificar aluno reprovado (média < 5.0)")
    void deveIdentificarAlunoReprovado() {
        // Arrange
        MediaFinal media = MediaFinal.criar(4.0);
        
        // Assert
        assertThat(media.reprovado()).isTrue();
        assertThat(media.aprovado()).isFalse();
        assertThat(media.emRecuperacao()).isFalse();
    }
    
    @Test
    @DisplayName("Deve identificar aluno em recuperação (5.0 <= média < 7.0)")
    void deveIdentificarAlunoEmRecuperacao() {
        // Arrange
        MediaFinal media = MediaFinal.criar(6.0);
        
        // Assert
        assertThat(media.emRecuperacao()).isTrue();
        assertThat(media.aprovado()).isFalse();
        assertThat(media.reprovado()).isFalse();
    }
    
    @Test
    @DisplayName("Deve considerar duas médias iguais")
    void deveConsiderarMediasIguais() {
        // Arrange
        MediaFinal media1 = MediaFinal.criar(7.5);
        MediaFinal media2 = MediaFinal.criar(7.5);
        
        // Assert
        assertThat(media1).isEqualTo(media2);
        assertThat(media1.hashCode()).isEqualTo(media2.hashCode());
    }
    
    @Test
    @DisplayName("Deve formatar média corretamente no toString")
    void deveFormatarMediaNoToString() {
        // Arrange
        MediaFinal media = MediaFinal.criar(7.5);
        
        // Assert
        assertThat(media.toString()).isEqualTo("7,50");
    }
}
