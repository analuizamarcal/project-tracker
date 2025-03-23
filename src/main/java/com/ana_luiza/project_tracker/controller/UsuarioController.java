package com.ana_luiza.project_tracker.controller;

import com.ana_luiza.project_tracker.dto.UsuarioCreateDTO;
import com.ana_luiza.project_tracker.dto.UsuarioViewDTO;
import com.ana_luiza.project_tracker.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Buscar todos os usuários (apenas ADMIN)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioViewDTO> findAll() {
        return usuarioService.findAll();
    }

    // Buscar um usuário por ID (ADMIN ou o próprio usuário)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UsuarioViewDTO> findById(@PathVariable Long id) {
        UsuarioViewDTO usuarioDTO = usuarioService.findById(id);
        return ResponseEntity.ok(usuarioDTO);
    }

    // Criar um novo usuário (apenas ADMIN)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioViewDTO> create(@RequestBody UsuarioCreateDTO usuarioDTO) {
        UsuarioViewDTO novoUsuarioDTO = usuarioService.create(usuarioDTO);
        return ResponseEntity.ok(novoUsuarioDTO);
    }

    // Atualizar um usuário existente (ADMIN ou o próprio usuário)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UsuarioViewDTO> update(@PathVariable Long id, @RequestBody UsuarioCreateDTO usuarioDTO) {
        UsuarioViewDTO usuarioAtualizadoDTO = usuarioService.update(id, usuarioDTO);
        return ResponseEntity.ok(usuarioAtualizadoDTO);
    }

    // Alterar senha do usuário (ADMIN ou o próprio usuário)
    @PatchMapping("/{id}/alterar-senha")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<Void> alterarSenha(@PathVariable Long id, @RequestBody Map<String, String> senhaPayload) {
        String senhaAtual = senhaPayload.get("senhaAtual");
        String novaSenha = senhaPayload.get("novaSenha");

        usuarioService.alterarSenha(id, senhaAtual, novaSenha);
        return ResponseEntity.noContent().build();
    }

    // Deletar um usuário (apenas ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
