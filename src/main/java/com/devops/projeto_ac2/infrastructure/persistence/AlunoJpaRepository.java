package com.devops.projeto_ac2.infrastructure.persistence;

import com.devops.projeto_ac2.domain.entities.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA Repository para Aluno
 * Implementação específica do Spring Data JPA
 */
@Repository
public interface AlunoJpaRepository extends JpaRepository<Aluno, Long> {
    
    @Query("SELECT a FROM Aluno a WHERE a.registroAcademico.valor = :ra")
    Optional<Aluno> findByRA(@Param("ra") String ra);
    
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Aluno a WHERE a.registroAcademico.valor = :ra")
    boolean existsByRA(@Param("ra") String ra);
    
    @Query("SELECT a FROM Aluno a WHERE a.concluiu = true")
    List<Aluno> findConcluidos();
    
    @Query("SELECT a FROM Aluno a WHERE a.concluiu = false")
    List<Aluno> findNaoConcluidos();
}
