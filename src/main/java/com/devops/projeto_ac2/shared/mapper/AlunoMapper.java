package com.devops.projeto_ac2.shared.mapper;

import com.devops.projeto_ac2.domain.entities.Aluno;
import com.devops.projeto_ac2.shared.dto.AlunoResponseDTO;
import com.devops.projeto_ac2.shared.dto.RankingResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Mapper para converter Aluno em DTOs
 */
@Component
public class AlunoMapper {
    
    /**
     * Converte Aluno entity para AlunoResponseDTO
     */
    public AlunoResponseDTO toResponseDTO(Aluno aluno) {
        if (aluno == null) {
            return null;
        }
        
        return AlunoResponseDTO.builder()
                .id(aluno.getId())
                .nome(aluno.getNome())
                .ra(aluno.getRegistroAcademico().getValor())
                .mediaFinal(aluno.getMediaFinal())
                .concluiu(aluno.isConcluiu())
                .cursosAdicionais(aluno.getCursosAdicionais())
                .tentativasAvaliacao(aluno.getTentativasAvaliacao())
                .tentativasRestantes(aluno.tentativasRestantes())
                .situacao(determinarSituacao(aluno))
                .dataCriacao(aluno.getDataCriacao())
                .dataAtualizacao(aluno.getDataAtualizacao())
                .dataConclusao(aluno.getDataConclusao())
                .build();
    }
    
    /**
     * Converte lista de Alunos para lista de RankingResponseDTO com posições
     */
    public List<RankingResponseDTO> toRankingResponseDTOList(List<Aluno> alunos) {
        if (alunos == null) {
            return List.of();
        }
        
        return IntStream.range(0, alunos.size())
                .mapToObj(i -> toRankingResponseDTO(alunos.get(i), i + 1))
                .collect(Collectors.toList());
    }
    
    /**
     * Converte Aluno entity para RankingResponseDTO
     */
    private RankingResponseDTO toRankingResponseDTO(Aluno aluno, int posicao) {
        return RankingResponseDTO.builder()
                .posicao(posicao)
                .alunoId(aluno.getId())
                .nome(aluno.getNome())
                .ra(aluno.getRegistroAcademico().getValor())
                .mediaFinal(aluno.getMediaFinal())
                .cursosAdicionais(aluno.getCursosAdicionais())
                .concluiu(aluno.isConcluiu())
                .situacao(determinarSituacao(aluno))
                .build();
    }
    
    private String determinarSituacao(Aluno aluno) {
        if (!aluno.isConcluiu()) {
            if (aluno.emRecuperacao()) {
                return "EM_RECUPERACAO";
            }
            return "NAO_CONCLUIDO";
        }
        
        if (aluno.aprovado()) {
            return "APROVADO";
        }
        
        if (aluno.reprovado()) {
            return "REPROVADO";
        }
        
        return "EM_RECUPERACAO";
    }
}
