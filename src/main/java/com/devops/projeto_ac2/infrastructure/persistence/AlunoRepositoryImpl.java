package com.devops.projeto_ac2.infrastructure.persistence;

import com.devops.projeto_ac2.domain.entities.Aluno;
import com.devops.projeto_ac2.domain.repositories.AlunoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter do AlunoRepository (interface do domínio) para AlunoJpaRepository (Spring Data JPA)
 * Seguindo Clean Architecture: implementação na infraestrutura, interface no domínio
 */
@Component
public class AlunoRepositoryImpl implements AlunoRepository {
    
    private final AlunoJpaRepository jpaRepository;
    
    public AlunoRepositoryImpl(AlunoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Aluno salvar(Aluno aluno) {
        return jpaRepository.save(aluno);
    }
    
    @Override
    public Optional<Aluno> buscarPorId(Long id) {
        return jpaRepository.findById(id);
    }
    
    @Override
    public Optional<Aluno> buscarPorRA(String ra) {
        return jpaRepository.findByRA(ra);
    }
    
    @Override
    public List<Aluno> buscarTodos() {
        return jpaRepository.findAll();
    }
    
    @Override
    public List<Aluno> buscarConcluidos() {
        return jpaRepository.findConcluidos();
    }
    
    @Override
    public List<Aluno> buscarNaoConcluidos() {
        return jpaRepository.findNaoConcluidos();
    }
    
    @Override
    public boolean existePorRA(String ra) {
        return jpaRepository.existsByRA(ra);
    }
    
    @Override
    public void deletar(Long id) {
        jpaRepository.deleteById(id);
    }
}
