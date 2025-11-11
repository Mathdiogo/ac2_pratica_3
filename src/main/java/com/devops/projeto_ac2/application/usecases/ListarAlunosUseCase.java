package com.devops.projeto_ac2.application.usecases;

import com.devops.projeto_ac2.domain.entities.Aluno;
import com.devops.projeto_ac2.domain.repositories.AlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Use Case: Listar todos os alunos
 */
@Service
@Transactional(readOnly = true)
public class ListarAlunosUseCase {
    
    private final AlunoRepository alunoRepository;
    
    public ListarAlunosUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }
    
    /**
     * Executa a listagem de todos os alunos
     */
    public List<Aluno> executar() {
        return alunoRepository.buscarTodos();
    }
    
    /**
     * Lista apenas alunos que concluíram
     */
    public List<Aluno> executarConcluidos() {
        return alunoRepository.buscarConcluidos();
    }
    
    /**
     * Lista apenas alunos que não concluíram
     */
    public List<Aluno> executarNaoConcluidos() {
        return alunoRepository.buscarNaoConcluidos();
    }
}
