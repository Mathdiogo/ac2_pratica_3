package com.devops.projeto_ac2.domain.entities;

import com.devops.projeto_ac2.domain.exceptions.DomainException;
import com.devops.projeto_ac2.domain.valueobjects.MediaFinal;
import com.devops.projeto_ac2.domain.valueobjects.NomeAluno;
import com.devops.projeto_ac2.domain.valueobjects.RegistroAcademico;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes unitários para Entity Aluno
 * Testando comportamentos de negócio
 */
@DisplayName("Testes da Entity Aluno")
class AlunoTest {
    
    @Test
    @DisplayName("Deve criar aluno com dados válidos")
    void deveCriarAlunoValido() {
        // Arrange
        NomeAluno nome = NomeAluno.criar("João Silva");
        RegistroAcademico ra = RegistroAcademico.criar("12345");
        
        // Act
        Aluno aluno = Aluno.criar(nome, ra);
        
        // Assert
        assertThat(aluno).isNotNull();
        assertThat(aluno.getNome()).isEqualTo("João Silva");
        assertThat(aluno.getRegistroAcademico().getValor()).isEqualTo("12345");
        assertThat(aluno.getMediaFinal()).isEqualTo(0.0);
        assertThat(aluno.isConcluiu()).isFalse();
        assertThat(aluno.getCursosAdicionais()).isEqualTo(0);
        assertThat(aluno.getDataCriacao()).isNotNull();
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao criar aluno com nome nulo")
    void deveLancarExcecaoQuandoNomeNulo() {
        // Arrange
        RegistroAcademico ra = RegistroAcademico.criar("12345");
        
        // Act & Assert
        assertThatThrownBy(() -> Aluno.criar(null, ra))
                .isInstanceOf(DomainException.class)
                .hasMessage("Nome do aluno não pode ser nulo");
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao criar aluno com RA nulo")
    void deveLancarExcecaoQuandoRANulo() {
        // Arrange
        NomeAluno nome = NomeAluno.criar("João Silva");
        
        // Act & Assert
        assertThatThrownBy(() -> Aluno.criar(nome, null))
                .isInstanceOf(DomainException.class)
                .hasMessage("RA do aluno não pode ser nulo");
    }
    
    @Test
    @DisplayName("Deve concluir curso com média válida")
    void deveConcluirCursoComMediaValida() {
        // Arrange
        Aluno aluno = criarAlunoTeste();
        MediaFinal media = MediaFinal.criar(8.5);
        
        // Act
        aluno.concluirCurso(media);
        
        // Assert
        assertThat(aluno.isConcluiu()).isTrue();
        assertThat(aluno.getMediaFinal()).isEqualTo(8.5);
        assertThat(aluno.getDataConclusao()).isNotNull();
    }
    
    @Test
    @DisplayName("Deve adicionar 3 cursos extras quando aprovado (média >= 7.0)")
    void deveAdicionarCursosExtrasQuandoAprovado() {
        // Arrange
        Aluno aluno = criarAlunoTeste();
        MediaFinal media = MediaFinal.criar(7.5);
        
        // Act
        aluno.concluirCurso(media);
        
        // Assert
        assertThat(aluno.getCursosAdicionais()).isEqualTo(3);
    }
    
    @Test
    @DisplayName("Não deve adicionar cursos extras quando reprovado")
    void naoDeveAdicionarCursosExtrasQuandoReprovado() {
        // Arrange
        Aluno aluno = criarAlunoTeste();
        MediaFinal media = MediaFinal.criar(4.0);
        
        // Act
        aluno.concluirCurso(media);
        
        // Assert
        assertThat(aluno.getCursosAdicionais()).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao concluir curso com média nula")
    void deveLancarExcecaoAoConcluirComMediaNula() {
        // Arrange
        Aluno aluno = criarAlunoTeste();
        
        // Act & Assert
        assertThatThrownBy(() -> aluno.concluirCurso(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("Média final não pode ser nula");
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao tentar concluir curso já concluído")
    void deveLancarExcecaoAoConcluirCursoJaConcluido() {
        // Arrange
        Aluno aluno = criarAlunoTeste();
        MediaFinal media1 = MediaFinal.criar(8.0);
        MediaFinal media2 = MediaFinal.criar(9.0);
        aluno.concluirCurso(media1);
        
        // Act & Assert
        assertThatThrownBy(() -> aluno.concluirCurso(media2))
                .isInstanceOf(DomainException.class)
                .hasMessage("Aluno já concluiu o curso");
    }
    
    @Test
    @DisplayName("Deve adicionar cursos extras corretamente")
    void deveAdicionarCursosExtras() {
        // Arrange
        Aluno aluno = criarAlunoTeste();
        
        // Act
        aluno.adicionarCursosExtras(5);
        
        // Assert
        assertThat(aluno.getCursosAdicionais()).isEqualTo(5);
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao adicionar quantidade inválida de cursos")
    void deveLancarExcecaoAoAdicionarQuantidadeInvalida() {
        // Arrange
        Aluno aluno = criarAlunoTeste();
        
        // Act & Assert
        assertThatThrownBy(() -> aluno.adicionarCursosExtras(0))
                .isInstanceOf(DomainException.class)
                .hasMessage("Quantidade de cursos deve ser positiva");
        
        assertThatThrownBy(() -> aluno.adicionarCursosExtras(-1))
                .isInstanceOf(DomainException.class)
                .hasMessage("Quantidade de cursos deve ser positiva");
    }
    
    @Test
    @DisplayName("Deve atualizar média quando aluno não concluiu")
    void deveAtualizarMediaQuandoNaoConcluiu() {
        // Arrange
        Aluno aluno = criarAlunoTeste();
        MediaFinal novaMedia = MediaFinal.criar(6.5);
        
        // Act
        aluno.atualizarMedia(novaMedia);
        
        // Assert
        assertThat(aluno.getMediaFinal()).isEqualTo(6.5);
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar média após conclusão")
    void deveLancarExcecaoAoAtualizarMediaAposConclusao() {
        // Arrange
        Aluno aluno = criarAlunoTeste();
        MediaFinal media = MediaFinal.criar(8.0);
        aluno.concluirCurso(media);
        MediaFinal novaMedia = MediaFinal.criar(9.0);
        
        // Act & Assert
        assertThatThrownBy(() -> aluno.atualizarMedia(novaMedia))
                .isInstanceOf(DomainException.class)
                .hasMessage("Não é possível alterar média de aluno que já concluiu");
    }
    
    @Test
    @DisplayName("Deve atualizar nome do aluno")
    void deveAtualizarNome() {
        // Arrange
        Aluno aluno = criarAlunoTeste();
        NomeAluno novoNome = NomeAluno.criar("Maria Santos");
        
        // Act
        aluno.atualizarNome(novoNome);
        
        // Assert
        assertThat(aluno.getNome()).isEqualTo("Maria Santos");
    }
    
    @Test
    @DisplayName("Deve identificar aluno aprovado corretamente")
    void deveIdentificarAlunoAprovado() {
        // Arrange
        Aluno aluno = criarAlunoTeste();
        MediaFinal media = MediaFinal.criar(8.0);
        aluno.concluirCurso(media);
        
        // Act & Assert
        assertThat(aluno.aprovado()).isTrue();
        assertThat(aluno.reprovado()).isFalse();
    }
    
    @Test
    @DisplayName("Deve identificar aluno reprovado corretamente")
    void deveIdentificarAlunoReprovado() {
        // Arrange
        Aluno aluno = criarAlunoTeste();
        MediaFinal media = MediaFinal.criar(4.0);
        aluno.concluirCurso(media);
        
        // Act & Assert
        assertThat(aluno.reprovado()).isTrue();
        assertThat(aluno.aprovado()).isFalse();
    }
    
    @Test
    @DisplayName("Deve identificar aluno em recuperação")
    void deveIdentificarAlunoEmRecuperacao() {
        // Arrange
        Aluno aluno = criarAlunoTeste();
        MediaFinal media = MediaFinal.criar(6.0);
        aluno.atualizarMedia(media);
        
        // Act & Assert
        assertThat(aluno.emRecuperacao()).isTrue();
    }
    
    // Método auxiliar para criar aluno de teste
    private Aluno criarAlunoTeste() {
        NomeAluno nome = NomeAluno.criar("João Silva");
        RegistroAcademico ra = RegistroAcademico.criar("12345");
        return Aluno.criar(nome, ra);
    }
}
