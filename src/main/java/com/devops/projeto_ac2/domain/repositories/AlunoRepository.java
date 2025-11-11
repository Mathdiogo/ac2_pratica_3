package com.devops.projeto_ac2.domain.repositories;

import com.devops.projeto_ac2.domain.entities.Aluno;

import java.util.List;
import java.util.Optional;

/**
 * Interface do repositório de Aluno (Port)
 * Seguindo Clean Architecture, a interface fica no domínio
 * e a implementação na camada de infraestrutura
 */
public interface AlunoRepository {
    
    /**
     * Salva um aluno (create ou update)
     */
    Aluno salvar(Aluno aluno);
    
    /**
     * Busca um aluno por ID
     */
    Optional<Aluno> buscarPorId(Long id);
    
    /**
     * Busca um aluno por RA
     */
    Optional<Aluno> buscarPorRA(String ra);
    
    /**
     * Busca todos os alunos
     */
    List<Aluno> buscarTodos();
    
    /**
     * Busca alunos que concluíram o curso
     */
    List<Aluno> buscarConcluidos();
    
    /**
     * Busca alunos que ainda não concluíram
     */
    List<Aluno> buscarNaoConcluidos();
    
    /**
     * Verifica se existe um aluno com o RA informado
     */
    boolean existePorRA(String ra);
    
    /**
     * Deleta um aluno por ID
     */
    void deletar(Long id);
}
