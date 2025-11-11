package com.devops.projeto_ac2.application.usecases;

import com.devops.projeto_ac2.domain.entities.Aluno;
import com.devops.projeto_ac2.domain.exceptions.AlunoNotFoundException;
import com.devops.projeto_ac2.domain.repositories.AlunoRepository;
import com.devops.projeto_ac2.domain.valueobjects.MediaFinal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case: Registrar tentativa de avaliação do aluno
 * Implementa regra de limite de 3 tentativas
 */
@Service
public class RegistrarTentativaUseCase {
    
    private final AlunoRepository alunoRepository;
    
    public RegistrarTentativaUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }
    
    /**
     * Executa o registro de uma tentativa de avaliação
     * 
     * @param alunoId ID do aluno
     * @param nota Nota obtida na tentativa
     * @return O aluno atualizado
     * @throws AlunoNotFoundException se o aluno não existir
     */
    @Transactional
    public Aluno executar(Long alunoId, double nota) {
        // Buscar aluno
        Aluno aluno = alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> new AlunoNotFoundException(alunoId));
        
        // Criar Value Object da média
        MediaFinal media = MediaFinal.criar(nota);
        
        // Registrar tentativa (validações são feitas na entidade)
        aluno.registrarTentativa(media);
        
        // Persistir mudanças
        return alunoRepository.salvar(aluno);
    }
}
