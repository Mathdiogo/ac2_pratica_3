package com.devops.projeto_ac2.shared.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para resposta de Aluno
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlunoResponseDTO {
    
    private Long id;
    private String nome;
    private String ra;
    private Double mediaFinal;
    private Boolean concluiu;
    private Integer cursosAdicionais;
    private Integer tentativasAvaliacao;
    private Integer tentativasRestantes;
    private String situacao; // APROVADO, REPROVADO, EM_RECUPERACAO, NAO_CONCLUIDO
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataCriacao;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataAtualizacao;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataConclusao;
}
