package com.ana_luiza.project_tracker.controller;

import com.ana_luiza.project_tracker.dto.ProjetoCreateDTO;
import com.ana_luiza.project_tracker.dto.ProjetoViewDTO;
import com.ana_luiza.project_tracker.service.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projetos")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    // Busca todos os projetos
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProjetoViewDTO> findAll() {
        return projetoService.findAll();
    }

    // Busca um projeto por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CONSULTOR') or @projetoService.isOwner(#id, authentication.principal.id)")
    public ResponseEntity<ProjetoViewDTO> findById(@PathVariable Long id) {
        ProjetoViewDTO projetoDTO = projetoService.findById(id);
        return ResponseEntity.ok(projetoDTO);
    }

    // Busca projetos por ID da equipe
    @GetMapping("/equipe/{equipeId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CONSULTOR') or @equipeService.isMember(#equipeId, authentication.principal.id)")
    public ResponseEntity<List<ProjetoViewDTO>> findByEquipeId(@PathVariable Long equipeId) {
        List<ProjetoViewDTO> projetos = projetoService.findByEquipeId(equipeId);
        return ResponseEntity.ok(projetos);
    }

    // Cria um novo projeto
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CONSULTOR')")
    public ResponseEntity<ProjetoViewDTO> create(@RequestBody ProjetoCreateDTO projetoCreateDTO) {
        ProjetoViewDTO novoProjetoDTO = projetoService.create(projetoCreateDTO);
        return ResponseEntity.ok(novoProjetoDTO);
    }

    // Atualiza um projeto existente
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @projetoService.isOwner(#id, authentication.principal.id)")
    public ResponseEntity<ProjetoViewDTO> update(@PathVariable Long id, @RequestBody ProjetoCreateDTO projetoCreateDTO) {
        ProjetoViewDTO projetoAtualizadoDTO = projetoService.update(id, projetoCreateDTO);
        return ResponseEntity.ok(projetoAtualizadoDTO);
    }

    // Deleta um projeto
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @projetoService.isOwner(#id, authentication.principal.id)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projetoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
