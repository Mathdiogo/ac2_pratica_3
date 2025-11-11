package com.devops.projeto_ac2.application.usecases;

import com.devops.projeto_ac2.domain.entities.Aluno;
import com.devops.projeto_ac2.domain.repositories.AlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Use Case: Obter ranking dos alunos
 * Ordena por média final (decrescente) e depois por cursos adicionais
 */
@Service
@Transactional(readOnly = true)
public class ObterRankingAlunosUseCase {
    
    private final AlunoRepository alunoRepository;
    
    public ObterRankingAlunosUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }
    
    /**
     * Executa a busca do ranking completo
     * Ordenação: 1º por média final (maior primeiro), 2º por cursos adicionais
     * 
     * @return Lista ordenada de alunos
     */
    public List<Aluno> executar() {
        return alunoRepository.buscarTodos().stream()
                .sorted(Comparator
                        .comparingDouble(Aluno::getMediaFinal).reversed()
                        .thenComparingInt(Aluno::getCursosAdicionais).reversed())
                .collect(Collectors.toList());
    }
    
    /**
     * Retorna apenas o top N alunos
     * 
     * @param limite Quantidade de alunos no topo do ranking
     * @return Lista com os N melhores alunos
     */
    public List<Aluno> executarTop(int limite) {
        if (limite <= 0) {
            throw new IllegalArgumentException("Limite deve ser maior que zero");
        }
        
        return alunoRepository.buscarTodos().stream()
                .sorted(Comparator
                        .comparingDouble(Aluno::getMediaFinal).reversed()
                        .thenComparingInt(Aluno::getCursosAdicionais).reversed())
                .limit(limite)
                .collect(Collectors.toList());
    }
    
    /**
     * Retorna ranking apenas dos alunos aprovados
     */
    public List<Aluno> executarAprovados() {
        return alunoRepository.buscarConcluidos().stream()
                .filter(Aluno::aprovado)
                .sorted(Comparator
                        .comparingDouble(Aluno::getMediaFinal).reversed()
                        .thenComparingInt(Aluno::getCursosAdicionais).reversed())
                .collect(Collectors.toList());
    }
}
