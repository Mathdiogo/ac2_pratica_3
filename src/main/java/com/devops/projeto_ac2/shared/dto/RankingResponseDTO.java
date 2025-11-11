package com.devops.projeto_ac2.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resposta de ranking
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingResponseDTO {
    
    private Integer posicao;
    private Long alunoId;
    private String nome;
    private String ra;
    private Double mediaFinal;
    private Integer cursosAdicionais;
    private Boolean concluiu;
    private String situacao;
}
