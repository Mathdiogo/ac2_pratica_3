package com.devops.projeto_ac2.application.usecases;

import com.devops.projeto_ac2.domain.entities.Aluno;
import com.devops.projeto_ac2.domain.exceptions.AlunoNotFoundException;
import com.devops.projeto_ac2.domain.repositories.AlunoRepository;
import com.devops.projeto_ac2.domain.valueobjects.MediaFinal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case: Concluir curso de um aluno
 * Aplica regras de negócio relacionadas à conclusão
 */
@Service
public class ConcluirCursoUseCase {
    
    private final AlunoRepository alunoRepository;
    
    public ConcluirCursoUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }
    
    /**
     * Executa o caso de uso de concluir o curso
     * 
     * @param alunoId ID do aluno
     * @param mediaFinal Média final obtida
     * @return O aluno atualizado
     * @throws AlunoNotFoundException se o aluno não existir
     */
    @Transactional
    public Aluno executar(Long alunoId, double mediaFinal) {
        // Buscar aluno
        Aluno aluno = alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> new AlunoNotFoundException(alunoId));
        
        // Criar Value Object da média (validação é feita no VO)
        MediaFinal mediaVO = MediaFinal.criar(mediaFinal);
        
        // Executar comportamento de negócio (na entidade)
        aluno.concluirCurso(mediaVO);
        
        // Persistir mudanças
        return alunoRepository.salvar(aluno);
    }
}
