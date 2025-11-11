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
    @PostMapping
    public ResponseEntity<AlunoResponseDTO> criar(@Valid @RequestBody CriarAlunoRequestDTO request) {
        Aluno aluno = criarAlunoUseCase.executar(request.getNome(), request.getRa());
        AlunoResponseDTO response = alunoMapper.toResponseDTO(aluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * GET /api/alunos/{id} - Buscar aluno por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscarPorId(@PathVariable Long id) {
        Aluno aluno = buscarAlunoPorIdUseCase.executar(id);
        AlunoResponseDTO response = alunoMapper.toResponseDTO(aluno);
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /api/alunos - Listar todos os alunos
     */
    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> listarTodos(
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
    @PostMapping("/{id}/tentativas")
    public ResponseEntity<AlunoResponseDTO> registrarTentativa(
            @PathVariable Long id,
            @Valid @RequestBody RegistrarTentativaRequestDTO request) {
        
        Aluno aluno = registrarTentativaUseCase.executar(id, request.getNota());
        AlunoResponseDTO response = alunoMapper.toResponseDTO(aluno);
        return ResponseEntity.ok(response);
    }
    
    /**
     * PATCH /api/alunos/{id}/concluir - Concluir curso do aluno
     */
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<AlunoResponseDTO> concluirCurso(
            @PathVariable Long id,
            @Valid @RequestBody ConcluirCursoRequestDTO request) {
        
        Aluno aluno = concluirCursoUseCase.executar(id, request.getMediaFinal());
        AlunoResponseDTO response = alunoMapper.toResponseDTO(aluno);
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /api/alunos/ranking - Obter ranking completo dos alunos
     */
    @GetMapping("/ranking")
    public ResponseEntity<List<RankingResponseDTO>> obterRanking(
            @RequestParam(required = false) Integer top,
            @RequestParam(required = false) Boolean apenasAprovados) {
        
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
