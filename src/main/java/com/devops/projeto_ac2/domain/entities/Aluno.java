package com.devops.projeto_ac2.domain.entities;

import com.devops.projeto_ac2.domain.exceptions.DomainException;
import com.devops.projeto_ac2.domain.valueobjects.MediaFinal;
import com.devops.projeto_ac2.domain.valueobjects.NomeAluno;
import com.devops.projeto_ac2.domain.valueobjects.RegistroAcademico;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade Aluno seguindo DDD
 * Entidade rica com comportamentos de negócio, não apenas getters/setters
 */
@Entity
@Table(name = "tb_alunos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Para JPA
public class Aluno {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Embedded
    @AttributeOverride(name = "valor", column = @Column(name = "ra", nullable = false, unique = true, length = 20))
    private RegistroAcademico registroAcademico;
    
    @Column(nullable = false)
    private double mediaFinal;
    
    @Column(nullable = false)
    private boolean concluiu;
    
    @Column(nullable = false)
    private int cursosAdicionais;
    
    @Column(nullable = false)
    private int tentativasAvaliacao;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;
    
    @Column
    private LocalDateTime dataAtualizacao;
    
    @Column
    private LocalDateTime dataConclusao;
    
    // Construtor privado para forçar uso do factory method
    private Aluno(NomeAluno nome, RegistroAcademico registroAcademico) {
        this.nome = nome.getValor();
        this.registroAcademico = registroAcademico;
        this.mediaFinal = 0.0;
        this.concluiu = false;
        this.cursosAdicionais = 0;
        this.tentativasAvaliacao = 0;
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    /**
     * Factory method para criar um novo aluno
     * Garante que o aluno sempre seja criado em um estado válido
     */
    public static Aluno criar(NomeAluno nome, RegistroAcademico registroAcademico) {
        if (nome == null) {
            throw new DomainException("Nome do aluno não pode ser nulo");
        }
        if (registroAcademico == null) {
            throw new DomainException("RA do aluno não pode ser nulo");
        }
        return new Aluno(nome, registroAcademico);
    }
    
    /**
     * Método de negócio: registra uma tentativa de avaliação
     * Valida se ainda há tentativas disponíveis
     */
    public void registrarTentativa(MediaFinal media) {
        if (media == null) {
            throw new DomainException("Média não pode ser nula");
        }
        
        if (this.concluiu) {
            throw new DomainException("Aluno já concluiu o curso");
        }
        
        if (this.tentativasAvaliacao >= 3) {
            throw new DomainException("Aluno já utilizou todas as 3 tentativas disponíveis");
        }
        
        this.tentativasAvaliacao++;
        this.mediaFinal = media.getValor();
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    /**
     * Método de negócio: conclui o curso do aluno com uma média final
     * Implementa a regra: se média >= 7.0, ganha 3 cursos adicionais
     * Implementa a regra: média >= 9.0 ganha bônus de 5 cursos extras
     */
    public void concluirCurso(MediaFinal media) {
        if (media == null) {
            throw new DomainException("Média final não pode ser nula");
        }
        
        if (this.concluiu) {
            throw new DomainException("Aluno já concluiu o curso");
        }
        
        if (this.tentativasAvaliacao == 0) {
            throw new DomainException("Aluno deve realizar pelo menos uma tentativa antes de concluir");
        }
        
        this.mediaFinal = media.getValor();
        this.concluiu = true;
        this.dataConclusao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
        
        // Regra de negócio: se média >= 9.0, ganha bônus de 5 cursos extras
        if (media.getValor() >= 9.0) {
            this.adicionarCursosExtras(5);
        }
        // Regra de negócio: se aprovado (>= 7.0), ganha 3 cursos adicionais
        else if (media.aprovado()) {
            this.adicionarCursosExtras(3);
        }
    }
    
    /**
     * Método de negócio: adiciona cursos extras ao aluno
     */
    public void adicionarCursosExtras(int quantidade) {
        if (quantidade <= 0) {
            throw new DomainException("Quantidade de cursos deve ser positiva");
        }
        this.cursosAdicionais += quantidade;
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    /**
     * Método de negócio: atualiza a média do aluno (apenas se não concluiu)
     */
    public void atualizarMedia(MediaFinal novaMedia) {
        if (novaMedia == null) {
            throw new DomainException("Média não pode ser nula");
        }
        
        if (this.concluiu) {
            throw new DomainException("Não é possível alterar média de aluno que já concluiu");
        }
        
        this.mediaFinal = novaMedia.getValor();
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    /**
     * Método de negócio: atualiza o nome do aluno
     */
    public void atualizarNome(NomeAluno novoNome) {
        if (novoNome == null) {
            throw new DomainException("Nome não pode ser nulo");
        }
        this.nome = novoNome.getValor();
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    /**
     * Verifica se o aluno foi aprovado
     */
    public boolean aprovado() {
        return this.concluiu && this.mediaFinal >= 7.0;
    }
    
    /**
     * Verifica se o aluno foi reprovado
     */
    public boolean reprovado() {
        return this.concluiu && this.mediaFinal < 5.0;
    }
    
    /**
     * Verifica se o aluno está em recuperação
     */
    public boolean emRecuperacao() {
        return !this.concluiu && this.mediaFinal >= 5.0 && this.mediaFinal < 7.0;
    }
    
    /**
     * Obtém o nome como Value Object
     */
    public NomeAluno getNomeVO() {
        return NomeAluno.criar(this.nome);
    }
    
    /**
     * Obtém a média como Value Object
     */
    public MediaFinal getMediaVO() {
        return MediaFinal.criar(this.mediaFinal);
    }
    
    /**
     * Verifica se aluno tem tentativas disponíveis
     */
    public boolean temTentativasDisponiveis() {
        return this.tentativasAvaliacao < 3;
    }
    
    /**
     * Retorna número de tentativas restantes
     */
    public int tentativasRestantes() {
        return 3 - this.tentativasAvaliacao;
    }
}
