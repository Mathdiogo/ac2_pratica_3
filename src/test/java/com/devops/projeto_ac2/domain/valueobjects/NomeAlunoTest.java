package com.devops.projeto_ac2.domain.valueobjects;

import com.devops.projeto_ac2.domain.exceptions.InvalidValueObjectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes unitários para NomeAluno (Value Object)
 */
@DisplayName("Testes do Value Object NomeAluno")
class NomeAlunoTest {
    
    @Test
    @DisplayName("Deve criar nome válido com sucesso")
    void deveCriarNomeValido() {
        // Arrange & Act
        NomeAluno nome = NomeAluno.criar("João Silva");
        
        // Assert
        assertThat(nome).isNotNull();
        assertThat(nome.getValor()).isEqualTo("João Silva");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando nome é nulo")
    void deveLancarExcecaoQuandoNomeNulo() {
        // Act & Assert
        assertThatThrownBy(() -> NomeAluno.criar(null))
                .isInstanceOf(InvalidValueObjectException.class)
                .hasMessage("Nome não pode ser nulo ou vazio");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando nome é vazio")
    void deveLancarExcecaoQuandoNomeVazio() {
        // Act & Assert
        assertThatThrownBy(() -> NomeAluno.criar(""))
                .isInstanceOf(InvalidValueObjectException.class)
                .hasMessage("Nome não pode ser nulo ou vazio");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando nome tem menos de 3 caracteres")
    void deveLancarExcecaoQuandoNomeMuitoCurto() {
        // Act & Assert
        assertThatThrownBy(() -> NomeAluno.criar("Jo"))
                .isInstanceOf(InvalidValueObjectException.class)
                .hasMessage("Nome deve ter no mínimo 3 caracteres");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando nome tem mais de 100 caracteres")
    void deveLancarExcecaoQuandoNomeMuitoLongo() {
        // Arrange
        String nomeLongo = "A".repeat(101);
        
        // Act & Assert
        assertThatThrownBy(() -> NomeAluno.criar(nomeLongo))
                .isInstanceOf(InvalidValueObjectException.class)
                .hasMessage("Nome deve ter no máximo 100 caracteres");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando nome contém números")
    void deveLancarExcecaoQuandoNomeContemNumeros() {
        // Act & Assert
        assertThatThrownBy(() -> NomeAluno.criar("João123"))
                .isInstanceOf(InvalidValueObjectException.class)
                .hasMessage("Nome deve conter apenas letras e espaços");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando nome contém caracteres especiais")
    void deveLancarExcecaoQuandoNomeContemCaracteresEspeciais() {
        // Act & Assert
        assertThatThrownBy(() -> NomeAluno.criar("João@Silva"))
                .isInstanceOf(InvalidValueObjectException.class)
                .hasMessage("Nome deve conter apenas letras e espaços");
    }
    
    @Test
    @DisplayName("Deve aceitar nome com acentuação")
    void deveAceitarNomeComAcentuacao() {
        // Arrange & Act
        NomeAluno nome = NomeAluno.criar("José María Ñoño");
        
        // Assert
        assertThat(nome.getValor()).isEqualTo("José María Ñoño");
    }
    
    @Test
    @DisplayName("Deve remover espaços em branco extras")
    void deveRemoverEspacosExtras() {
        // Arrange & Act
        NomeAluno nome = NomeAluno.criar("  João Silva  ");
        
        // Assert
        assertThat(nome.getValor()).isEqualTo("João Silva");
    }
    
    @Test
    @DisplayName("Deve retornar primeiro nome corretamente")
    void deveRetornarPrimeiroNome() {
        // Arrange
        NomeAluno nome = NomeAluno.criar("João Silva Santos");
        
        // Act & Assert
        assertThat(nome.primeiroNome()).isEqualTo("João");
    }
    
    @Test
    @DisplayName("Deve considerar dois nomes iguais")
    void deveConsiderarNomesIguais() {
        // Arrange
        NomeAluno nome1 = NomeAluno.criar("João Silva");
        NomeAluno nome2 = NomeAluno.criar("João Silva");
        
        // Assert
        assertThat(nome1).isEqualTo(nome2);
        assertThat(nome1.hashCode()).isEqualTo(nome2.hashCode());
    }
    
    @Test
    @DisplayName("Deve retornar valor no toString")
    void deveRetornarValorNoToString() {
        // Arrange
        NomeAluno nome = NomeAluno.criar("João Silva");
        
        // Assert
        assertThat(nome.toString()).isEqualTo("João Silva");
    }
}
