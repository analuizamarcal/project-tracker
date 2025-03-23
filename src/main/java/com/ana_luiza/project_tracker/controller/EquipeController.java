package com.ana_luiza.project_tracker.controller;

import com.ana_luiza.project_tracker.dto.EquipeCreateDTO;
import com.ana_luiza.project_tracker.dto.EquipeViewDTO;
import com.ana_luiza.project_tracker.service.EquipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipes")
public class EquipeController {

    @Autowired
    private EquipeService equipeService;

    // Busca todas as equipes
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<EquipeViewDTO> findAll() {
        return equipeService.findAll();
    }

    // Busca uma equipe por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @equipeService.isMember(#id, authentication.principal.id)")
    public ResponseEntity<EquipeViewDTO> findById(@PathVariable Long id) {
        EquipeViewDTO equipeDTO = equipeService.findById(id);
        return ResponseEntity.ok(equipeDTO);
    }

    // Cria uma nova equipe (o usuário autenticado será o admin automaticamente)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EquipeViewDTO> create(@RequestBody EquipeCreateDTO equipeDTO) {
        EquipeViewDTO novaEquipeDTO = equipeService.create(equipeDTO);
        return ResponseEntity.ok(novaEquipeDTO);
    }

    // Atualiza uma equipe existente
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @equipeService.isAdmin(#id, authentication.principal.id)")
    public ResponseEntity<EquipeViewDTO> update(@PathVariable Long id, @RequestBody EquipeCreateDTO equipeDTO) {
        EquipeViewDTO equipeAtualizadaDTO = equipeService.update(id, equipeDTO);
        return ResponseEntity.ok(equipeAtualizadaDTO);
    }

    // Deleta uma equipe
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @equipeService.isAdmin(#id, authentication.principal.id)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        equipeService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/usuarios") // Adicionando "usuarios"
    @PreAuthorize("hasRole('ADMIN') or @equipeService.isAdmin(#id, authentication.principal.id)")
    public ResponseEntity<EquipeViewDTO> adicionarUsuarios(@PathVariable Long id, @RequestBody List<Long> usuariosIds) {
        EquipeViewDTO equipeAtualizadaDTO = equipeService.adicionarMembros(id, usuariosIds);
        return ResponseEntity.ok(equipeAtualizadaDTO);
    }
}
