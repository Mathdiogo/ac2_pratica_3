package com.devops.projeto_ac2.domain.valueobjects;

import com.devops.projeto_ac2.domain.exceptions.InvalidValueObjectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes unitários para RegistroAcademico (Value Object)
 * Seguindo TDD: testes primeiro!
 */
@DisplayName("Testes do Value Object RegistroAcademico")
class RegistroAcademicoTest {
    
    @Test
    @DisplayName("Deve criar RA válido com sucesso")
    void deveCriarRAValido() {
        // Arrange & Act
        RegistroAcademico ra = RegistroAcademico.criar("12345ABC");
        
        // Assert
        assertThat(ra).isNotNull();
        assertThat(ra.getValor()).isEqualTo("12345ABC");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando RA é nulo")
    void deveLancarExcecaoQuandoRANulo() {
        // Act & Assert
        assertThatThrownBy(() -> RegistroAcademico.criar(null))
                .isInstanceOf(InvalidValueObjectException.class)
                .hasMessage("RA não pode ser nulo ou vazio");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando RA é vazio")
    void deveLancarExcecaoQuandoRAVazio() {
        // Act & Assert
        assertThatThrownBy(() -> RegistroAcademico.criar(""))
                .isInstanceOf(InvalidValueObjectException.class)
                .hasMessage("RA não pode ser nulo ou vazio");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando RA tem menos de 5 caracteres")
    void deveLancarExcecaoQuandoRAMuitoCurto() {
        // Act & Assert
        assertThatThrownBy(() -> RegistroAcademico.criar("1234"))
                .isInstanceOf(InvalidValueObjectException.class)
                .hasMessage("RA deve ter no mínimo 5 caracteres");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando RA tem mais de 20 caracteres")
    void deveLancarExcecaoQuandoRAMuitoLongo() {
        // Act & Assert
        assertThatThrownBy(() -> RegistroAcademico.criar("123456789012345678901"))
                .isInstanceOf(InvalidValueObjectException.class)
                .hasMessage("RA deve ter no máximo 20 caracteres");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando RA contém caracteres especiais")
    void deveLancarExcecaoQuandoRATemCaracteresEspeciais() {
        // Act & Assert
        assertThatThrownBy(() -> RegistroAcademico.criar("12345@#$"))
                .isInstanceOf(InvalidValueObjectException.class)
                .hasMessage("RA deve conter apenas letras e números");
    }
    
    @Test
    @DisplayName("Deve aceitar RA com letras e números")
    void deveAceitarRAComLetrasENumeros() {
        // Arrange & Act
        RegistroAcademico ra = RegistroAcademico.criar("ABC123XYZ");
        
        // Assert
        assertThat(ra.getValor()).isEqualTo("ABC123XYZ");
    }
    
    @Test
    @DisplayName("Deve considerar dois RAs iguais com mesmo valor")
    void deveConsiderarRAsIguais() {
        // Arrange
        RegistroAcademico ra1 = RegistroAcademico.criar("12345");
        RegistroAcademico ra2 = RegistroAcademico.criar("12345");
        
        // Assert
        assertThat(ra1).isEqualTo(ra2);
        assertThat(ra1.hashCode()).isEqualTo(ra2.hashCode());
    }
    
    @Test
    @DisplayName("Deve retornar valor no toString")
    void deveRetornarValorNoToString() {
        // Arrange
        RegistroAcademico ra = RegistroAcademico.criar("12345");
        
        // Assert
        assertThat(ra.toString()).isEqualTo("12345");
    }
}
