package com.devops.projeto_ac2.application.usecases;

import com.devops.projeto_ac2.domain.entities.Aluno;
import com.devops.projeto_ac2.domain.exceptions.DomainException;
import com.devops.projeto_ac2.domain.repositories.AlunoRepository;
import com.devops.projeto_ac2.domain.valueobjects.NomeAluno;
import com.devops.projeto_ac2.domain.valueobjects.RegistroAcademico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case: Criar um novo aluno
 * Seguindo Clean Architecture, encapsula toda a lógica de criação
 */
@Service
public class CriarAlunoUseCase {
    
    private final AlunoRepository alunoRepository;
    
    public CriarAlunoUseCase(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }
    
    /**
     * Executa o caso de uso de criar um aluno
     * 
     * @param nome Nome do aluno
     * @param ra RA do aluno
     * @return O aluno criado
     * @throws DomainException se o RA já existir ou dados forem inválidos
     */
    @Transactional
    public Aluno executar(String nome, String ra) {
        // Validar se RA já existe
        if (alunoRepository.existePorRA(ra)) {
            throw new DomainException("Já existe um aluno cadastrado com o RA: " + ra);
        }
        
        // Criar Value Objects (validações são feitas nos VOs)
        NomeAluno nomeVO = NomeAluno.criar(nome);
        RegistroAcademico raVO = RegistroAcademico.criar(ra);
        
        // Criar entidade usando factory method
        Aluno aluno = Aluno.criar(nomeVO, raVO);
        
        // Persistir
        return alunoRepository.salvar(aluno);
    }
}
