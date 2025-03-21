package com.ana_luiza.project_tracker.controller;

import com.ana_luiza.project_tracker.dto.TarefaDTO;
import com.ana_luiza.project_tracker.service.TarefaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
public class TarefaController {

    private final TarefaService tarefaService;

    // Criar uma tarefa (somente dono do projeto)
    @PostMapping("/{projetoId}")
    @PreAuthorize("@projetoService.usuarioTemAcesso(#projetoId)")
    public ResponseEntity<TarefaDTO> criarTarefa(@PathVariable Long projetoId, @RequestBody TarefaDTO tarefaDTO) {
        TarefaDTO tarefaCriada = tarefaService.criarTarefa(projetoId, tarefaDTO);
        return ResponseEntity.ok(tarefaCriada);
    }

    // Listar todas as tarefas de um projeto (somente ADMIN ou dono do projeto)
    @GetMapping("/{projetoId}")
    @PreAuthorize("hasRole('ADMIN') or @projetoService.usuarioTemAcesso(#projetoId)")
    public ResponseEntity<List<TarefaDTO>> listarTarefas(@PathVariable Long projetoId) {
        List<TarefaDTO> tarefas = tarefaService.listarTarefas(projetoId);
        return ResponseEntity.ok(tarefas);
    }

    // Buscar uma tarefa específica (somente ADMIN ou dono do projeto)
    @GetMapping("/detalhes/{tarefaId}")
    @PreAuthorize("hasRole('ADMIN') or @projetoService.usuarioTemAcesso(@tarefaService.buscarProjetoIdPorTarefa(#tarefaId))")
    public ResponseEntity<TarefaDTO> buscarTarefaPorId(@PathVariable Long tarefaId) {
        TarefaDTO tarefa = tarefaService.buscarTarefaPorId(tarefaId);
        return ResponseEntity.ok(tarefa);
    }

    // Atualizar uma tarefa (somente dono do projeto)
    @PutMapping("/{tarefaId}")
    @PreAuthorize("@projetoService.usuarioTemAcesso(@tarefaService.buscarProjetoIdPorTarefa(#tarefaId))")
    public ResponseEntity<TarefaDTO> atualizarTarefa(@PathVariable Long tarefaId, @RequestBody TarefaDTO tarefaDTO) {
        TarefaDTO tarefaAtualizada = tarefaService.atualizarTarefa(tarefaId, tarefaDTO);
        return ResponseEntity.ok(tarefaAtualizada);
    }

    // Excluir uma tarefa (somente dono do projeto)
    @DeleteMapping("/{tarefaId}")
    @PreAuthorize("@projetoService.usuarioTemAcesso(@tarefaService.buscarProjetoIdPorTarefa(#tarefaId))")
    public ResponseEntity<Void> excluirTarefa(@PathVariable Long tarefaId) {
        tarefaService.excluirTarefa(tarefaId);
        return ResponseEntity.noContent().build();
    }
}
