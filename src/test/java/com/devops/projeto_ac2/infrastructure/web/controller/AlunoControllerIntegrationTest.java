package com.devops.projeto_ac2.infrastructure.web.controller;

import com.devops.projeto_ac2.domain.entities.Aluno;
import com.devops.projeto_ac2.domain.repositories.AlunoRepository;
import com.devops.projeto_ac2.domain.valueobjects.NomeAluno;
import com.devops.projeto_ac2.domain.valueobjects.RegistroAcademico;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para AlunoController
 * Testa a API REST completa com contexto Spring
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Testes de Integração do Controller de Alunos")
class AlunoControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private AlunoRepository alunoRepository;
    
    @BeforeEach
    void setUp() {
        // Limpar dados antes de cada teste
        alunoRepository.buscarTodos().forEach(aluno -> alunoRepository.deletar(aluno.getId()));
    }
    
    @Test
    @DisplayName("POST /api/alunos - Deve criar aluno com sucesso")
    void deveCriarAlunoComSucesso() throws Exception {
        // Arrange
        Map<String, String> request = new HashMap<>();
        request.put("nome", "João Silva");
        request.put("ra", "12345ABC");
        
        // Act & Assert
        mockMvc.perform(post("/api/alunos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.ra").value("12345ABC"))
                .andExpect(jsonPath("$.mediaFinal").value(0.0))
                .andExpect(jsonPath("$.concluiu").value(false))
                .andExpect(jsonPath("$.cursosAdicionais").value(0))
                .andExpect(jsonPath("$.situacao").value("NAO_CONCLUIDO"));
    }
    
    @Test
    @DisplayName("POST /api/alunos - Deve retornar erro 400 quando dados são inválidos")
    void deveRetornarErro400QuandoDadosInvalidos() throws Exception {
        // Arrange
        Map<String, String> request = new HashMap<>();
        request.put("nome", "Jo"); // nome muito curto
        request.put("ra", "123"); // RA muito curto
        
        // Act & Assert
        mockMvc.perform(post("/api/alunos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Erro de validação"))
                .andExpect(jsonPath("$.fieldErrors").isArray());
    }
    
    @Test
    @DisplayName("POST /api/alunos - Deve retornar erro quando RA já existe")
    void deveRetornarErroQuandoRAJaExiste() throws Exception {
        // Arrange - criar aluno primeiro
        Aluno aluno = Aluno.criar(
                NomeAluno.criar("João Silva"), 
                RegistroAcademico.criar("12345ABC")
        );
        alunoRepository.salvar(aluno);
        
        Map<String, String> request = new HashMap<>();
        request.put("nome", "Maria Santos");
        request.put("ra", "12345ABC"); // mesmo RA
        
        // Act & Assert
        mockMvc.perform(post("/api/alunos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Erro de regra de negócio"))
                .andExpect(jsonPath("$.message").value(containsString("Já existe um aluno cadastrado com o RA")));
    }
    
    @Test
    @DisplayName("GET /api/alunos/{id} - Deve buscar aluno por ID")
    void deveBuscarAlunoPorId() throws Exception {
        // Arrange
        Aluno aluno = Aluno.criar(
                NomeAluno.criar("João Silva"), 
                RegistroAcademico.criar("12345ABC")
        );
        Aluno salvo = alunoRepository.salvar(aluno);
        
        // Act & Assert
        mockMvc.perform(get("/api/alunos/" + salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(salvo.getId()))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.ra").value("12345ABC"));
    }
    
    @Test
    @DisplayName("GET /api/alunos/{id} - Deve retornar 404 quando aluno não existe")
    void deveRetornar404QuandoAlunoNaoExiste() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/alunos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Aluno não encontrado"));
    }
    
    @Test
    @DisplayName("GET /api/alunos - Deve listar todos os alunos")
    void deveListarTodosAlunos() throws Exception {
        // Arrange
        Aluno aluno1 = Aluno.criar(NomeAluno.criar("João Silva"), RegistroAcademico.criar("12345"));
        Aluno aluno2 = Aluno.criar(NomeAluno.criar("Maria Santos"), RegistroAcademico.criar("67890"));
        alunoRepository.salvar(aluno1);
        alunoRepository.salvar(aluno2);
        
        // Act & Assert
        mockMvc.perform(get("/api/alunos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }
    
    @Test
    @DisplayName("PATCH /api/alunos/{id}/concluir - Deve concluir curso com sucesso")
    void deveConcluirCursoComSucesso() throws Exception {
        // Arrange
        Aluno aluno = Aluno.criar(
                NomeAluno.criar("João Silva"), 
                RegistroAcademico.criar("12345ABC")
        );
        Aluno salvo = alunoRepository.salvar(aluno);
        
        Map<String, Double> request = new HashMap<>();
        request.put("mediaFinal", 8.5);
        
        // Act & Assert
        mockMvc.perform(patch("/api/alunos/" + salvo.getId() + "/concluir")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.concluiu").value(true))
                .andExpect(jsonPath("$.mediaFinal").value(8.5))
                .andExpect(jsonPath("$.cursosAdicionais").value(3))
                .andExpect(jsonPath("$.situacao").value("APROVADO"));
    }
    
    @Test
    @DisplayName("PATCH /api/alunos/{id}/concluir - Deve validar média inválida")
    void deveValidarMediaInvalida() throws Exception {
        // Arrange
        Aluno aluno = Aluno.criar(
                NomeAluno.criar("João Silva"), 
                RegistroAcademico.criar("12345ABC")
        );
        Aluno salvo = alunoRepository.salvar(aluno);
        
        Map<String, Double> request = new HashMap<>();
        request.put("mediaFinal", 11.0); // média inválida
        
        // Act & Assert
        mockMvc.perform(patch("/api/alunos/" + salvo.getId() + "/concluir")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("GET /api/alunos?concluido=true - Deve filtrar alunos concluídos")
    void deveFiltrarAlunosConcluidos() throws Exception {
        // Arrange
        Aluno aluno1 = Aluno.criar(NomeAluno.criar("João Silva"), RegistroAcademico.criar("12345"));
        Aluno aluno2 = Aluno.criar(NomeAluno.criar("Maria Santos"), RegistroAcademico.criar("67890"));
        aluno1 = alunoRepository.salvar(aluno1);
        aluno2 = alunoRepository.salvar(aluno2);
        
        // Concluir apenas o primeiro
        aluno1.concluirCurso(com.devops.projeto_ac2.domain.valueobjects.MediaFinal.criar(8.0));
        alunoRepository.salvar(aluno1);
        
        // Act & Assert
        mockMvc.perform(get("/api/alunos?concluido=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].concluiu").value(true));
    }
}
