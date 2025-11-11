package com.devops.projeto_ac2.application.usecases;

import com.devops.projeto_ac2.domain.entities.Aluno;
import com.devops.projeto_ac2.domain.exceptions.AlunoNotFoundException;
import com.devops.projeto_ac2.domain.repositories.AlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case: Buscar aluno por ID
 */
@Service
@Transactional(readOnly = true)
public class BuscarAlunoPorIdUseCase {
    
    private final AlunoRepository alunoRepository;
    
    public BuscarAlunoPorIdUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }
    
    /**
     * Executa a busca de aluno por ID
     * 
     * @param id ID do aluno
     * @return O aluno encontrado
     * @throws AlunoNotFoundException se não encontrar
     */
    public Aluno executar(Long id) {
        return alunoRepository.buscarPorId(id)
                .orElseThrow(() -> new AlunoNotFoundException(id));
    }
}
