package com.devops.projeto_ac2.shared.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para criação de Aluno
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CriarAlunoRequestDTO {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "Nome deve conter apenas letras e espaços")
    private String nome;
    
    @NotBlank(message = "RA é obrigatório")
    @Size(min = 5, max = 20, message = "RA deve ter entre 5 e 20 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "RA deve conter apenas letras e números")
    private String ra;
}
