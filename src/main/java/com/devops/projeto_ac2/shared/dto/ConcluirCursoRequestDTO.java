package com.devops.projeto_ac2.shared.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para conclusão de curso
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConcluirCursoRequestDTO {
    
    @NotNull(message = "Média final é obrigatória")
    @DecimalMin(value = "0.0", message = "Média final não pode ser negativa")
    @DecimalMax(value = "10.0", message = "Média final não pode ser maior que 10.0")
    private Double mediaFinal;
}
