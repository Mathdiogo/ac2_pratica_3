package com.devops.projeto_ac2.infrastructure.web.controller;

import com.devops.projeto_ac2.application.usecases.BuscarAlunoPorIdUseCase;
import com.devops.projeto_ac2.application.usecases.ConcluirCursoUseCase;
import com.devops.projeto_ac2.application.usecases.CriarAlunoUseCase;
import com.devops.projeto_ac2.application.usecases.ListarAlunosUseCase;
import com.devops.projeto_ac2.application.usecases.RegistrarTentativaUseCase;
import com.devops.projeto_ac2.application.usecases.ObterRankingAlunosUseCase;
import com.devops.projeto_ac2.domain.entities.Aluno;
import com.devops.projeto_ac2.shared.dto.AlunoResponseDTO;
import com.devops.projeto_ac2.shared.dto.ConcluirCursoRequestDTO;
import com.devops.projeto_ac2.shared.dto.CriarAlunoRequestDTO;
import com.devops.projeto_ac2.shared.dto.RegistrarTentativaRequestDTO;
import com.devops.projeto_ac2.shared.dto.RankingResponseDTO;
import com.devops.projeto_ac2.shared.mapper.AlunoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller REST para gerenciamento de Alunos
 * Seguindo Clean Architecture: Controller na camada de infraestrutura (web)
 */
@RestController
@RequestMapping("/api/alunos")
@CrossOrigin(origins = "*")
@Tag(name = "Alunos", description = "API de gerenciamento de alunos")
public class AlunoController {
    
    private final CriarAlunoUseCase criarAlunoUseCase;
    private final ConcluirCursoUseCase concluirCursoUseCase;
    private final BuscarAlunoPorIdUseCase buscarAlunoPorIdUseCase;
    private final ListarAlunosUseCase listarAlunosUseCase;
    private final RegistrarTentativaUseCase registrarTentativaUseCase;
    private final ObterRankingAlunosUseCase obterRankingAlunosUseCase;
    private final AlunoMapper alunoMapper;
    
    public AlunoController(
            CriarAlunoUseCase criarAlunoUseCase,
            ConcluirCursoUseCase concluirCursoUseCase,
            BuscarAlunoPorIdUseCase buscarAlunoPorIdUseCase,
            ListarAlunosUseCase listarAlunosUseCase,
            RegistrarTentativaUseCase registrarTentativaUseCase,
            ObterRankingAlunosUseCase obterRankingAlunosUseCase,
            AlunoMapper alunoMapper) {
        this.criarAlunoUseCase = criarAlunoUseCase;
        this.concluirCursoUseCase = concluirCursoUseCase;
        this.buscarAlunoPorIdUseCase = buscarAlunoPorIdUseCase;
        this.listarAlunosUseCase = listarAlunosUseCase;
        this.registrarTentativaUseCase = registrarTentativaUseCase;
        this.obterRankingAlunosUseCase = obterRankingAlunosUseCase;
        this.alunoMapper = alunoMapper;
    }
    
    /**
     * POST /api/alunos - Criar novo aluno
     */
    @Operation(summary = "Criar novo aluno", description = "Cria um novo aluno com nome e RA únicos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Aluno criado com sucesso",
                    content = @Content(schema = @Schema(implementation = AlunoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou RA já existente"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<AlunoResponseDTO> criar(@Valid @RequestBody CriarAlunoRequestDTO request) {
        Aluno aluno = criarAlunoUseCase.executar(request.getNome(), request.getRa());
        AlunoResponseDTO response = alunoMapper.toResponseDTO(aluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * GET /api/alunos/{id} - Buscar aluno por ID
     */
    @Operation(summary = "Buscar aluno por ID", description = "Retorna os dados completos de um aluno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aluno encontrado",
                    content = @Content(schema = @Schema(implementation = AlunoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscarPorId(
            @Parameter(description = "ID do aluno") @PathVariable Long id) {
        Aluno aluno = buscarAlunoPorIdUseCase.executar(id);
        AlunoResponseDTO response = alunoMapper.toResponseDTO(aluno);
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /api/alunos - Listar todos os alunos
     */
    @Operation(summary = "Listar alunos", description = "Lista todos os alunos com filtro opcional de conclusão")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> listarTodos(
            @Parameter(description = "Filtrar por conclusão: true (concluídos), false (não concluídos), null (todos)")
            @RequestParam(required = false) Boolean concluido) {
        
        List<Aluno> alunos;
        
        if (concluido != null) {
            if (concluido) {
                alunos = listarAlunosUseCase.executarConcluidos();
            } else {
                alunos = listarAlunosUseCase.executarNaoConcluidos();
            }
        } else {
            alunos = listarAlunosUseCase.executar();
        }
        
        List<AlunoResponseDTO> response = alunos.stream()
                .map(alunoMapper::toResponseDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * POST /api/alunos/{id}/tentativas - Registrar tentativa de avaliação
     */
    @Operation(summary = "Registrar tentativa", description = "Registra uma tentativa de avaliação (máximo 3 tentativas)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tentativa registrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado"),
            @ApiResponse(responseCode = "409", description = "Limite de tentativas esgotado")
    })
    @PostMapping("/{id}/tentativas")
    public ResponseEntity<AlunoResponseDTO> registrarTentativa(
            @Parameter(description = "ID do aluno") @PathVariable Long id,
            @Valid @RequestBody RegistrarTentativaRequestDTO request) {
        
        Aluno aluno = registrarTentativaUseCase.executar(id, request.getNota());
        AlunoResponseDTO response = alunoMapper.toResponseDTO(aluno);
        return ResponseEntity.ok(response);
    }
    
    /**
     * PATCH /api/alunos/{id}/concluir - Concluir curso do aluno
     */
    @Operation(summary = "Concluir curso", description = "Finaliza o curso do aluno aplicando bônus conforme média")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso concluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "Aluno já concluiu ou sem tentativas"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<AlunoResponseDTO> concluirCurso(
            @Parameter(description = "ID do aluno") @PathVariable Long id,
            @Valid @RequestBody ConcluirCursoRequestDTO request) {
        
        Aluno aluno = concluirCursoUseCase.executar(id, request.getMediaFinal());
        AlunoResponseDTO response = alunoMapper.toResponseDTO(aluno);
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /api/alunos/ranking - Obter ranking completo dos alunos
     */
    @Operation(summary = "Obter ranking", description = "Retorna ranking dos alunos ordenado por média e cursos extras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ranking retornado com sucesso")
    })
    @GetMapping("/ranking")
    public ResponseEntity<List<RankingResponseDTO>> obterRanking(
            @Parameter(description = "Limitar top N alunos") @RequestParam(required = false) Integer top,
            @Parameter(description = "Filtrar apenas aprovados") @RequestParam(required = false) Boolean apenasAprovados) {
        
        List<Aluno> alunos;
        
        if (apenasAprovados != null && apenasAprovados) {
            alunos = obterRankingAlunosUseCase.executarAprovados();
        } else if (top != null && top > 0) {
            alunos = obterRankingAlunosUseCase.executarTop(top);
        } else {
            alunos = obterRankingAlunosUseCase.executar();
        }
        
        List<RankingResponseDTO> response = alunoMapper.toRankingResponseDTOList(alunos);
        return ResponseEntity.ok(response);
    }
}
