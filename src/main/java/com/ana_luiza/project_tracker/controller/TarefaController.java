package com.ana_luiza.project_tracker.controller;

import com.ana_luiza.project_tracker.dto.TarefaCreateDTO;
import com.ana_luiza.project_tracker.dto.TarefaViewDTO;
import com.ana_luiza.project_tracker.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    // Busca todas as tarefas
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CONSULTOR')")
    public List<TarefaViewDTO> findAll() {
        return tarefaService.findAll();
    }

    // Busca uma tarefa por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CONSULTOR') or @tarefaService.isResponsavel(#id, authentication.principal.id)")
    public ResponseEntity<TarefaViewDTO> findById(@PathVariable Long id) {
        TarefaViewDTO tarefaDTO = tarefaService.findById(id);
        return ResponseEntity.ok(tarefaDTO);
    }

    // Busca tarefas por ID do projeto
    @GetMapping("/projeto/{projetoId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CONSULTOR') or @projetoService.isOwner(#projetoId, authentication.principal.id)")
    public ResponseEntity<List<TarefaViewDTO>> findByProjetoId(@PathVariable Long projetoId) {
        List<TarefaViewDTO> tarefas = tarefaService.findByProjetoId(projetoId);
        return ResponseEntity.ok(tarefas);
    }

    // Busca tarefas por ID do responsável (usuário)
    @GetMapping("/responsavel/{responsavelId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CONSULTOR') or #responsavelId == authentication.principal.id")
    public ResponseEntity<List<TarefaViewDTO>> findByResponsavelId(@PathVariable Long responsavelId) {
        List<TarefaViewDTO> tarefas = tarefaService.findByResponsavelId(responsavelId);
        return ResponseEntity.ok(tarefas);
    }

    // Cria uma nova tarefa
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CONSULTOR')")
    public ResponseEntity<TarefaViewDTO> create(@RequestBody TarefaCreateDTO tarefaDTO) {
        TarefaViewDTO novaTarefaDTO = tarefaService.create(tarefaDTO);
        return ResponseEntity.ok(novaTarefaDTO);
    }

    // Atualiza uma tarefa existente
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @tarefaService.isResponsavel(#id, authentication.principal.id)")
    public ResponseEntity<TarefaViewDTO> update(@PathVariable Long id, @RequestBody TarefaCreateDTO tarefaDTO) {
        TarefaViewDTO tarefaAtualizadaDTO = tarefaService.update(id, tarefaDTO);
        return ResponseEntity.ok(tarefaAtualizadaDTO);
    }

    // Deleta uma tarefa
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @tarefaService.isResponsavel(#id, authentication.principal.id)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tarefaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}